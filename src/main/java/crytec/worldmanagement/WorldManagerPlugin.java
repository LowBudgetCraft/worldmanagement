package crytec.worldmanagement;

import co.aikar.commands.BukkitCommandManager;
import crytec.worldmanagement.data.WorldConfiguration;
import crytec.worldmanagement.metrics.Metrics;
import java.io.IOException;
import net.crytec.inventoryapi.InventoryAPI;
import net.crytec.libs.commons.utils.CommonsAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldManagerPlugin extends JavaPlugin {

  private static WorldManagerPlugin instance;
  private WorldManager worldManager;

  @Override
  public void onLoad() {
    ConfigurationSerialization.registerClass(WorldConfiguration.class);
    WorldManagerPlugin.instance = this;
    saveResource("config.yml", false);
  }

  @Override
  public void onEnable() {

    try {
      Language.initialize(this);
    } catch (IOException e) {
      e.printStackTrace();
    }

    new CommonsAPI(this);
    new InventoryAPI(this);

    worldManager = new WorldManager(this);
    Bukkit.getPluginManager().registerEvents(new WorldListener(this, worldManager), this);
    worldManager.initialize();

    Metrics metrics = new Metrics(this);
    metrics.addCustomChart(new Metrics.SimplePie("loaded_worlds", () -> {
      int worlds = getWorldManager().getWorldConfigurations().size();
      if (worlds < 15) {
        return String.valueOf(worlds);
      } else if (worlds > 15 && worlds < 25) {
        return "15-25";
      } else if (worlds > 25 && worlds < 35) {
        return "25-35";
      } else {
        return "35+ - Thats crazy!";
      }
    }));

    metrics.addCustomChart(new Metrics.SimplePie("worldguard_purge", () -> getConfig().getBoolean("deletion.worldguard", true) ? "Enabled" : "Disabled"));

    BukkitCommandManager cm = new BukkitCommandManager(this);
    cm.registerCommand(new WorldCommands());
  }

  @Override
  public void onDisable() {
    getWorldManager().shutdown();
  }

  public static WorldManagerPlugin getInstance() {
    return WorldManagerPlugin.instance;
  }

  public WorldManager getWorldManager() {
    return worldManager;
  }

}