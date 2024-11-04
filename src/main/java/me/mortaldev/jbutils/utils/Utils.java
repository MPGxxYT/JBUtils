package me.mortaldev.jbutils.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

public class Utils {

  /**
   * Returns a new LinkedHashMap with the same key-value mappings as the given original
   * LinkedHashMap, but in reverse order. The original LinkedHashMap is not modified.
   *
   * @param original The LinkedHashMap to reverse.
   * @return A new LinkedHashMap with the same mappings, but in reverse order.
   */
  public static <K, V> LinkedHashMap<K, V> reverseMap(LinkedHashMap<K, V> original) {

    LinkedHashMap<K, V> reversed = new LinkedHashMap<>();
    ListIterator<Map.Entry<K, V>> iterator =
        new ArrayList<>(original.entrySet()).listIterator(original.size());

    while (iterator.hasPrevious()) {
      Map.Entry<K, V> entry = iterator.previous();
      reversed.put(entry.getKey(), entry.getValue());
    }

    return reversed;
  }

  /**
   * Converts the given ItemStack into a formatted item name string.
   *
   * @param itemStack The ItemStack to convert.
   * @return The formatted item name string.
   */
  public static String itemName(ItemStack itemStack) {
    return itemName(itemStack.getType());
  }

  /**
   * Converts the given Material into a formatted item name string.
   *
   * <p>This method splits the Material's key by underscores, capitalizes the first letter of each
   * word, and then joins them back together with spaces in between. For example, {@code
   * Material.LAPIS_LAZULI} would result in the string "Lapis Lazuli".
   *
   * @param material The Material to convert.
   * @return The formatted item name string.
   */
  public static String itemName(Material material) {
    StringBuilder name = new StringBuilder();
    String[] parts = material.getKey().getKey().split("_");
    for (String part : parts) {
      if (!part.isEmpty()) {
        name.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
      }
      name.append(' ');
    }
    return name.toString().trim();
  }
}
