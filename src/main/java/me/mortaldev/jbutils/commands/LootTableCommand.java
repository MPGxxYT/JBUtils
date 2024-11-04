package me.mortaldev.jbutils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import java.util.Optional;
import me.mortaldev.jbutils.Main;
import me.mortaldev.jbutils.menus.LootTableMenu;
import me.mortaldev.jbutils.modules.loottable.LootTable;
import me.mortaldev.jbutils.modules.loottable.LootTableManager;
import me.mortaldev.jbutils.utils.ItemStackHelper;
import me.mortaldev.jbutils.utils.TextUtil;
import org.bukkit.entity.Player;

@CommandAlias("loottable|ltb")
@CommandPermission("jbutils.admin")
@SuppressWarnings("unused")
public class LootTableCommand extends BaseCommand {

  @Default
  public void openMenu(final Player player) {
    Main.getGuiManager().openGUI(new LootTableMenu(), player);
  }

  @CommandAlias("roll")
  @CommandCompletion("@loottables @range:1-20")
  @Syntax("<LootTable> <Amount>")
  public void roll(final Player player, String table, Integer count) {
    Optional<LootTable> tableOptional = LootTableManager.getInstance().getByID(table);
    if (tableOptional.isEmpty()) {
      player.sendMessage(TextUtil.format("&cLoot Table not found."));
      return;
    }
    LootTable tableByID = tableOptional.get();
    if (tableByID.getChanceMap().size() < 1) {
      player.sendMessage(TextUtil.format("&cTable is empty."));
      return;
    }
    for (int i = 0; i < count; i++) {
      player.getInventory().addItem(ItemStackHelper.deserialize(tableByID.getChanceMap().roll()));
    }
  }

}
