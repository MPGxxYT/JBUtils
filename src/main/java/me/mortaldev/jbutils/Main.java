package me.mortaldev.jbutils;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import co.aikar.commands.PaperCommandManager;
import me.mortaldev.jbutils.commands.LootTableCommand;
import me.mortaldev.jbutils.modules.LootTable;
import me.mortaldev.jbutils.modules.LootTableManager;
import me.mortaldev.menuapi.GUIListener;
import me.mortaldev.menuapi.GUIManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;

public final class Main extends JavaPlugin {

  private static final String LABEL = "JBUtils";
  static Main instance;
  static HashSet<String> hardDependency = new HashSet<>();
  static HashSet<String> softDependency =
      new HashSet<>() {
        {
          add("Skript");
        }
      };
  static PaperCommandManager commandManager;
  static GUIManager guiManager;

  public static Main getInstance() {
    return instance;
  }

  public static String getLabel() {
    return LABEL;
  }

  public static GUIManager getGuiManager() {
    return guiManager;
  }

  public static void log(String message) {
    Bukkit.getLogger().info("[" + Main.getLabel() + "] " + message);
  }

  @Override
  public void onEnable() {
    instance = this;
    commandManager = new PaperCommandManager(this);

    // DATA FOLDER

    if (!getDataFolder().exists()) {
      getDataFolder().mkdir();
    }

    // DEPENDENCIES

    for (String plugin : hardDependency) {
      if (Bukkit.getPluginManager().getPlugin(plugin) == null) {
        getLogger().warning("Could not find " + plugin + "! This plugin is required.");
        Bukkit.getPluginManager().disablePlugin(this);
        return;
      }
    }

    // CONFIGS
    //    mainConfig = new MainConfig();

    // Managers (Loading data)
    LootTableManager.getLootTableManager().loadTables();

    // GUI Manager
    guiManager = new GUIManager();
    GUIListener guiListener = new GUIListener(guiManager);
    Bukkit.getPluginManager().registerEvents(guiListener, this);

    // Events

    //    getServer().getPluginManager().registerEvents(new OnGangCommand(), this);

    // COMMANDS

    if (Bukkit.getPluginManager().getPlugin("Skript") != null) {
      registerSkript();
      log("Registered Skript API Connection");
    }

    commandManager
        .getCommandCompletions()
        .registerCompletion(
            "loottables",
            c ->
                LootTableManager.getLootTableManager().getLootTables().stream()
                    .map(LootTable::getID)
                    .collect(Collectors.toSet()));

    commandManager.registerCommand(new LootTableCommand());

    getLogger().info(LABEL + " Enabled");
  }

  private void registerSkript() {
    SkriptAddon addon = Skript.registerAddon(this);
    try {
      addon.loadClasses("me.mortaldev.jbutils.register", "expressions");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onDisable() {
    getLogger().info(LABEL + " Disabled");
  }
}
