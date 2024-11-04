package me.mortaldev.jbutils.modules.loottable;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import me.mortaldev.crudapi.CRUD;
import me.mortaldev.jbutils.utils.ChanceMap;
import me.mortaldev.jbutils.utils.ItemStackHelper;
import me.mortaldev.jbutils.utils.TextUtil;
import me.mortaldev.jbutils.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LootTable implements CRUD.Identifiable {
  private final String id;
  private final ChanceMap<String> chanceMap = new ChanceMap<>(); // <item, chance>

  public LootTable(String id) {
    this.id = TextUtil.fileFormat(id);
  }

  public ChanceMap<String> getChanceMap() {
    return chanceMap;
  }

  public LinkedHashMap<ItemStack, BigDecimal> getTable() {
    chanceMap.sort();
    LinkedHashMap<ItemStack, BigDecimal> table = new LinkedHashMap<>();
    for (Map.Entry<String, BigDecimal> entry : chanceMap.getTable().entrySet()) {
      ItemStack item = ItemStackHelper.deserialize(entry.getKey());
      table.put(item, entry.getValue());
    }
    return table;
  }

  public void addItem(ItemStack item) {
    chanceMap.put(ItemStackHelper.serialize(item), true);
  }

  public void removeItem(ItemStack item) {
    chanceMap.remove(ItemStackHelper.serialize(item), true);
  }

  public void updateItem(ItemStack item, BigDecimal chance) {
    chanceMap.updateKey(ItemStackHelper.serialize(item), chance);
  }

  // Display

  public ItemStack getDisplay() {
    Material displayMaterial = Material.COBBLESTONE;
    Iterator<String> iterator = chanceMap.getTable().keySet().iterator();
    if (iterator.hasNext()) {
      displayMaterial = ItemStackHelper.deserialize(iterator.next()).getType();
    }
    ItemStackHelper.Builder builder =
        ItemStackHelper.builder(displayMaterial).name("&3&l" + id).addLore("&7ID: " + id);
    if (!chanceMap.getTable().isEmpty()) {
      builder.addLore().addLore("&3Contents:");
      chanceMap
          .getTable()
          .forEach(
              (serialized, chance) ->
                  builder.addLore(
                      TextUtil.format("&7 - &f" + chance + "% &7")
                          .append(
                              TextUtil.format(
                                  "&7"
                                      + Utils.itemName(ItemStackHelper.deserialize(serialized))))));
    }
    return builder.build();
  }

  @Override
  public String getID() {
    return this.id;
  }
}
