package me.mortaldev.jbutils.modules;

import me.mortaldev.jbutils.Main;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;

public enum LootTableManager {
  INSTANCE;

  public static LootTableManager getLootTableManager() {
    return INSTANCE;
  }

  private HashSet<LootTable> set;

  public void loadTables() {
    set = new HashSet<>();
    File mineDir = new File(LootTableCRUD.getInstance().getPath());
    if (!mineDir.exists()) {
      if (!mineDir.mkdirs()) {
        Main.log("Failed to /loottables/ create directory.");
        return;
      }
    }
    File[] files = mineDir.listFiles();
    if (files == null) {
      Main.log("No mines loaded.");
      return;
    }
    for (File file : files) {
      String fileNameWithoutExtension = file.getName().replace(".json", "");
      Optional<LootTable> data = LootTableCRUD.getInstance().getData(fileNameWithoutExtension);
      if (data.isEmpty()) {
        Main.log("Failed to load LootTable: " + file.getName());
        continue;
      }
      set.add(data.get());
    }
  }

  public LootTable getTableByID(String id) {
    for (LootTable lootTable : getLootTables()) {
      if (lootTable.getID().equals(id)) {
        return lootTable;
      }
    }
    return null;
  }

  public HashSet<LootTable> getLootTables() {
    if (set.isEmpty()) {
      loadTables();
    }
    return set;
  }

  public boolean containsLootTable(LootTable table) {
    for (LootTable lootTable : getLootTables()) {
      if (lootTable.getID().equals(table.getID())) {
        return true;
      }
    }
    return false;
  }

  public synchronized boolean addLootTable(LootTable table) {
    if (containsLootTable(table)) {
      return true;
    }
    set.add(table);
    table.save();
    return false;
  }

  public synchronized void removeLootTable(LootTable table) {
    if (containsLootTable(table)) {
      set.remove(table);
      table.delete();
    }
  }

  public synchronized void updateLootTable(LootTable table) {
    removeLootTable(table);
    addLootTable(table);
  }

}
