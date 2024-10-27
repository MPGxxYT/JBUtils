package me.mortaldev.jbutils.modules;

import me.mortaldev.crudapi.CRUD;
import me.mortaldev.jbutils.utils.ChanceMap;
import me.mortaldev.jbutils.utils.ItemStackHelper;
import me.mortaldev.jbutils.utils.TextUtil;
import me.mortaldev.jbutils.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LootTable implements CRUD.Identifiable {
  final String id;
  ChanceMap<String> chanceMap = new ChanceMap<>(); // <item, chance>

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
      for (Map.Entry<String, BigDecimal> entry : chanceMap.getTable().entrySet()) {
        ItemStack item = ItemStackHelper.deserialize(entry.getKey());
        Component displayName = TextUtil.format("&7" + Utils.itemName(item));
        if (item.getItemMeta().hasDisplayName()) {
          displayName = item.getItemMeta().displayName();
        }
        builder.addLore(
            TextUtil.format("&7 - &f" + entry.getValue() + "% &7" + item.getAmount() + "x &7")
                .append(displayName));
      }
    }
    return builder.build();
  }

  // CRUD Methods

  public void save() {
    LootTableCRUD.getInstance().saveData(this);
  }

  public void delete() {
    LootTableCRUD.getInstance().deleteData(this);
  }

  @Override
  public String getID() {
    return this.id;
  }
}
