package me.mortaldev.jbutils.modules;

import me.mortaldev.crudapi.CRUD;
import me.mortaldev.crudapi.CRUDManager;
import me.mortaldev.jbutils.Main;

public class LootTableManager extends CRUDManager<LootTable> {

  private LootTableManager() {}

  private static final class SingletonHolder {
    private static final LootTableManager instance = new LootTableManager();
  }

  public static LootTableManager getInstance() {
    return SingletonHolder.instance;
  }
  @Override
  public CRUD<LootTable> getCRUD() {
    return LootTableCRUD.getInstance();
  }

  @Override
  public void log(String string) {
    Main.log(string);
  }
}
