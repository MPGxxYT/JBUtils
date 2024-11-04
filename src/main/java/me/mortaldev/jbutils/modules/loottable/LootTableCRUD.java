package me.mortaldev.jbutils.modules.loottable;

import java.util.HashMap;
import me.mortaldev.crudapi.CRUD;
import me.mortaldev.jbutils.Main;

public class LootTableCRUD extends CRUD<LootTable> {

  private LootTableCRUD() {}

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

  private static class SingletonHelper {
    private static final LootTableCRUD INSTANCE = new LootTableCRUD();
  }
}
