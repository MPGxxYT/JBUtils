package me.mortaldev.jbutils.menus;

import me.mortaldev.jbutils.Main;
import me.mortaldev.jbutils.modules.LootTable;
import me.mortaldev.jbutils.modules.LootTableManager;
import me.mortaldev.jbutils.utils.ItemStackHelper;
import me.mortaldev.jbutils.utils.SupplierRunnable;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LootTableMenu extends InventoryGUI {
  String search = "";
  Set<LootTable> lootTables;

  public LootTableMenu(String search) {
    this.search = search;
    this.lootTables = LootTableManager.getInstance().getSet();
  }

  public LootTableMenu() {
    this.lootTables = LootTableManager.getInstance().getSet();
  }

  @Override
  protected Inventory createInventory() {
    if (!search.isBlank()) {
      return Bukkit.createInventory(null, 6 * 9, TextUtil.format("&3&lLoot Table: &f" + search));
    }
    return Bukkit.createInventory(null, 6 * 9, TextUtil.format("&3&lLoot Tables"));
  }

  @Override
  public void decorate(Player player) {
    ItemStack whiteGlass =
        ItemStackHelper.builder(Material.WHITE_STAINED_GLASS_PANE).name("").build();
    for (int i = 45; i < 54; i++) {
      this.getInventory().setItem(i, whiteGlass);
    }
    int i = 0;
    for (LootTable table : filterWithSearch()) {
      addButton(i, LootTableButton(table));
      i++;
    }
    addButton(49, SearchButton());
    addButton(53, CreateButton());
    super.decorate(player);
  }

  public Set<LootTable> filterWithSearch() {
    if (search.isBlank()) {
      return this.lootTables;
    }
    Set<LootTable> filteredTable = new HashSet<>();
    for (LootTable table : this.lootTables) {
      if (table.getID().contains(search)) {
        filteredTable.add(table);
      }
    }
    return filteredTable;
  }

  private InventoryButton CreateButton() {
    return new InventoryButton()
        .creator(
            player ->
                ItemStackHelper.builder(Material.LIME_DYE)
                    .name("&2&lCreate")
                    .addLore("&7Click to create a new Loot Table.")
                    .build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              new AnvilGUI.Builder()
                  .plugin(Main.getInstance())
                  .title("Create Loot Table")
                  .itemLeft(ItemStackHelper.builder(Material.WHITE_WOOL).name("~").build())
                  .onClick(
                      (slot, stateSnapshot) -> {
                        if (slot == 2) {
                          String text = stateSnapshot.getText();
                          LootTable lootTable = new LootTable(text);
                          if (LootTableManager.getInstance().add(lootTable)) {
                            Main.getGuiManager()
                                .openGUI(new ModifyLootTableMenu(lootTable), player);
                            return Collections.emptyList();
                          }
                          player.sendMessage(TextUtil.format("&cLoot Table already exists."));
                          Main.getGuiManager().openGUI(new LootTableMenu(), player);
                        }
                        return Collections.emptyList();
                      })
                  .open(player);
            });
  }

  private InventoryButton SearchButton() {
    return new InventoryButton()
        .creator(
            player -> {
              ItemStackHelper.Builder builder =
                  ItemStackHelper.builder(Material.ANVIL).name("&f&lSearch");
              if (search.isBlank()) {
                builder.addLore().addLore("&7[Click to Search]");
              } else {
                builder
                    .addLore()
                    .addLore("&fQuery: &7" + search)
                    .addLore()
                    .addLore("&7[Click to Clear Search]");
              }
              return builder.build();
            })
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              if (search.isBlank()) {
                new AnvilGUI.Builder()
                    .plugin(Main.getInstance())
                    .title("Lookup Loot Table")
                    .itemLeft(ItemStackHelper.builder(Material.PAPER).name("~").build())
                    .onClick(
                        (slot, stateSnapshot) -> {
                          if (slot == 2) {
                            String text = stateSnapshot.getText();
                            String updatedText = text.trim().replaceAll("^\\W*|\\W*$", "");
                            Main.getGuiManager()
                                .openGUI(new LootTableMenu(updatedText), player);
                          }
                          return Collections.emptyList();
                        })
                    .open(player);
              } else {
                Main.getGuiManager().openGUI(new LootTableMenu(), player);
              }
            });
  }

  private InventoryButton LootTableButton(LootTable table) {
    return new InventoryButton()
        .creator(
            player ->
                ItemStackHelper.builder(table.getDisplay())
                    .addLore("&7", "&7[Left-Click to Modify]", "&7[Right-Click to Delete]")
                    .build())
        .consumer(
            event -> {
              Player player = (Player) event.getWhoClicked();
              if (event.getClick() == ClickType.LEFT) {
                Main.getGuiManager().openGUI(new ModifyLootTableMenu(table), player);
              } else {
                ConfirmActionMenu confirmActionMenu = new ConfirmActionMenu(
                    ItemStackHelper.builder(table.getDisplay()).name("&c&lDelete?").build(),
                    new SupplierRunnable<>(
                        "&7Yes, delete this Loot Table.",
                        () -> {
                          LootTableManager.getInstance().remove(table);
                          Main.getGuiManager().openGUI(new LootTableMenu(search), player);
                        }),
                    new SupplierRunnable<>(
                        "&7No, don't delete this Loot Table.",
                        () -> Main.getGuiManager().openGUI(new LootTableMenu(search), player)));

                Main.getGuiManager().openGUI(confirmActionMenu, player);
              }
            });
  }
}
