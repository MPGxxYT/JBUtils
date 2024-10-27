package me.mortaldev.jbutils.register.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.mortaldev.jbutils.modules.LootTable;
import me.mortaldev.jbutils.modules.LootTableManager;
import me.mortaldev.jbutils.utils.ItemStackHelper;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ExrOfLootTable extends SimpleExpression<ItemStack> {

  static {
    Skript.registerExpression(ExrOfLootTable.class, ItemStack.class, ExpressionType.PROPERTY, "%number% of loottable %string%");
  }
  private Expression<Number> count;
  private Expression<String> id;

  @Override
  protected @Nullable ItemStack[] get(Event event) {
    LootTable table = LootTableManager.getLootTableManager().getTableByID(id.getSingle(event));
    if (table == null) {
      return null;
    }
    int countSingle = Integer.parseInt(count.getSingle(event).toString());
    ItemStack[] itemStacks = new ItemStack[countSingle];
    for (int i = 0; i < countSingle; i++) {
      itemStacks[i] = ItemStackHelper.deserialize(table.getChanceMap().roll());
    }
    return itemStacks;
  }

  @Override
  public boolean isSingle() {
    return false;
  }

  @Override
  public Class<? extends ItemStack> getReturnType() {
    return ItemStack.class;
  }

  @Override
  public String toString(@Nullable Event event, boolean b) {
    return count.getSingle(event) + "number of loottable " + id.getSingle(event);
  }

  @Override
  public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
    count = (Expression<Number>) exprs[0];
    id = (Expression<String>) exprs[1];
    return true;
  }
}
