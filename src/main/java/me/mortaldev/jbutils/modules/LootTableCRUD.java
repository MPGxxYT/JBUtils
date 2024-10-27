package me.mortaldev.jbutils.modules;

import me.mortaldev.crudapi.CRUD;
import me.mortaldev.jbutils.Main;

import java.util.HashMap;

public class LootTableCRUD extends CRUD<LootTable> {

  private LootTableCRUD() {}

  private static class SingletonHelper {
    private static final LootTableCRUD INSTANCE = new LootTableCRUD();
  }

  public static LootTableCRUD getInstance(){
    return SingletonHelper.INSTANCE;
  }

  @Override
  public Class<LootTable> getClazz() {
    return LootTable.class;
  }

  @Override
  public HashMap<Class<?>, Object> getTypeAdapterHashMap() {
    return new HashMap<>();
  }

  @Override
  public String getPath() {
    return Main.getInstance().getDataFolder().getAbsolutePath()+"/loottables/";
  }
}
