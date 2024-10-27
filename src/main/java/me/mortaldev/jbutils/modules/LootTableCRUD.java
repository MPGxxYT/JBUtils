package me.mortaldev.jbutils.modules;

import me.mortaldev.crudapi.CRUD;
import me.mortaldev.jbutils.Main;

import java.util.Optional;

public class LootTableCRUD extends CRUD<LootTable> {

  private static class SingletonHelper {
    private static final LootTableCRUD LOOT_TABLE_INSTANCE = new LootTableCRUD();
  }

  public static LootTableCRUD getInstance(){
    return SingletonHelper.LOOT_TABLE_INSTANCE;
  }

  @Override
  public String getPath() {
    return Main.getInstance().getDataFolder().getAbsolutePath()+"/loottables/";
  }

  protected Optional<LootTable> getData(String id) {
    return super.getData(id, LootTable.class);
  }
}
