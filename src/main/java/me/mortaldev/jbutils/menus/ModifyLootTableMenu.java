package me.mortaldev.jbutils.menus;

import me.mortaldev.jbutils.Main;
import me.mortaldev.jbutils.modules.LootTable;
import me.mortaldev.jbutils.modules.LootTableManager;
import me.mortaldev.jbutils.utils.ItemStackHelper;
import me.mortaldev.jbutils.utils.TextUtil;
import me.mortaldev.menuapi.InventoryButton;
import me.mortaldev.menuapi.InventoryGUI;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

public class ModifyLootTableMenu extends InventoryGUI {

  LootTable lootTable;

  public ModifyLootTableMenu(LootTable lootTable) {
    this.lootTable = lootTable;
    allowBottomInventoryClick(true);
  }

  @Override
  protected Inventory createInventory() {
    return Bukkit.createInventory(
        null, 6 * 9, TextUtil.format("&3&lLoot Table: &f" + lootTable.getID()));
  }

  @Override
  public void decorate(Player player) {
    ItemStack whiteGlass =
        ItemStackHelper.builder(Material.WHITE_STAINED_GLASS_PANE).name("").build();
    for (int i = 45; i < 54; i++) {
      this.getInventory().setItem(i, whiteGlass);
    }
    int i = 0;
    for (Map.Entry<ItemStack, BigDecimal> entry : lootTable.getTable().entrySet()) {
      addButton(i, ItemButton(entry.getKey(), entry.getValue()));
      i++;
    }
    addButton(49, AddItem());
    addButton(47, RebalanceButton());
    addButton(45, BackButton());
    super.decorate(player);
  }

  private InventoryButton ItemButton(ItemStack itemStack, BigDecimal chance) {
    return new InventoryButton()
        .creator(
            player ->
                ItemStackHelper.builder(itemStack.clone())
                    .addLore()
                    .addLore("&7Chance: " + chance.toString() + "%")
                    .addLore()
                    .addLore("&7[Left-Click to Change]")
                    .addLore("&7[Right-Click to Remove]")
                    .build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              if (event.getClick() == ClickType.LEFT) {
                new AnvilGUI.Builder()
                    .plugin(Main.getInstance())
                    .title("Change Chance")
                    .itemLeft(
                        ItemStackHelper.builder(Material.PAPER).name(chance.toString()).build())
                    .onClick(
                        (slot, stateSnapshot) -> {
                          if (slot == 2) {
                            String textEntry = stateSnapshot.getText();
                            if (textEntry.trim().matches("^(\\d+(\\.\\d+)?)$")) {
                              lootTable.updateItem(itemStack, new BigDecimal(textEntry));
                              LootTableManager.getInstance().update(lootTable);
                              Main.getGuiManager().openGUI(new ModifyLootTableMenu(lootTable), player);
                            }
                          }
                          return Collections.emptyList();
                        })
                    .open(player);
              } else {
                lootTable.removeItem(itemStack);
                LootTableManager.getInstance().update(lootTable);
                Main.getGuiManager().openGUI(new ModifyLootTableMenu(lootTable), player);
              }
            });
  }

  private InventoryButton AddItem() {
    return new InventoryButton()
        .creator(
            player ->
                ItemStackHelper.builder(Material.BUCKET)
                    .name("&e&lAdd Item")
                    .addLore("&7Click with an item to add.")
                    .build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              ItemStack itemStack = event.getCursor();
              if (itemStack == null) {
                return;
              }
              if (itemStack.getType().isAir()) {
                return;
              }
              lootTable.addItem(itemStack);
              LootTableManager.getInstance().update(lootTable);
              Main.getGuiManager().openGUI(new ModifyLootTableMenu(lootTable), player);
            });
  }

  private InventoryButton RebalanceButton() {
    return new InventoryButton()
        .creator(
            player ->
                ItemStackHelper.builder(Material.REDSTONE)
                    .name("&e&lRebalance")
                    .addLore("&7Will rebalance the percents")
                    .addLore("&7to have a sum of 100.")
                    .addLore()
                    .addLore("&eTotal: " + lootTable.getChanceMap().getTotal() + "%")
                    .addLore("")
                    .addLore("&7[Click to Rebalance]")
                    .build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              if (!lootTable.getChanceMap().balanceTable()) {
                return;
              }
              LootTableManager.getInstance().update(lootTable);
              Main.getGuiManager().openGUI(new ModifyLootTableMenu(lootTable), player);
            });
  }

  private InventoryButton BackButton() {
    return new InventoryButton()
        .creator(
            player ->
                ItemStackHelper.builder(Material.ARROW)
                    .name("&c&lBack")
                    .addLore("&7Click to go to previous menu.")
                    .build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              Main.getGuiManager().openGUI(new LootTableMenu(), player);
            });
  }
}
