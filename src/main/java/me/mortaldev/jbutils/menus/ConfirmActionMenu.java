package me.mortaldev.jbutils.menus;

import me.mortaldev.jbutils.utils.ItemStackHelper;
import me.mortaldev.jbutils.utils.SupplierRunnable;
import me.mortaldev.jbutils.utils.TextUtil;
import me.mortaldev.menuapi.InventoryButton;
import me.mortaldev.menuapi.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ConfirmActionMenu extends InventoryGUI {

  SupplierRunnable<String> confirmAction;
  SupplierRunnable<String> cancelAction;
  ItemStack centerItem;

  public ConfirmActionMenu(
      ItemStack centerItem,
      SupplierRunnable<String> confirmAction,
      SupplierRunnable<String> cancelAction) {
    this.confirmAction = confirmAction;
    this.cancelAction = cancelAction;
    this.centerItem = centerItem;
  }

  @Override
  protected Inventory createInventory() {
    return Bukkit.createInventory(null, 9 * 3, TextUtil.format("&3&lConfirm?"));
  }

  @Override
  public void decorate(Player player) {
    for (int i = 9; i < 12; i++) {
      addButton(i, ConfirmButton());
    }
    for (int i = 15; i < 18; i++) {
      addButton(i, CancelButton());
    }
    getInventory().setItem(13, centerItem);
    super.decorate(player);
  }

  private InventoryButton ConfirmButton() {
    return new InventoryButton()
        .creator(
            player ->
                ItemStackHelper.builder(Material.LIME_STAINED_GLASS_PANE)
                    .name("&2&lCONFIRM")
                    .addLore(confirmAction.get())
                    .build())
        .consumer(
            event -> confirmAction.run());
  }

  private InventoryButton CancelButton() {
    return new InventoryButton()
        .creator(
            player ->
                ItemStackHelper.builder(Material.RED_STAINED_GLASS_PANE)
                    .name("&c&lCANCEL")
                    .addLore(cancelAction.get())
                    .build())
        .consumer(
            event -> cancelAction.run());
  }
}
