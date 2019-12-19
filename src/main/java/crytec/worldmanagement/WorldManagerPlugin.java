package crytec.worldmanagement;

import co.aikar.commands.BukkitCommandManager;
import crytec.worldmanagement.metrics.Metrics;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldManagerPlugin extends JavaPlugin implements Listener {

  private static WorldManagerPlugin instance;
  private WorldManager worldManager;

  @Override
  public void onLoad() {
    WorldManagerPlugin.instance = this;
    saveResource("config.yml", false);
  }

  @Override
  public void onEnable() {
    loadLanguage();

    Bukkit.getPluginManager().registerEvents(new WorldListener(this), this);
    Bukkit.getPluginManager().registerEvents(this, this);

		worldManager = new WorldManager(this);
		worldManager.startup();

    Metrics metrics = new Metrics(this);
    metrics.addCustomChart(new Metrics.SimplePie("loaded_worlds", new Callable<String>() {
      @Override
      public String call() {
        int worlds = getWorldManager().getWorldConfigurations().size();
				if (worlds < 15)
					return String.valueOf(worlds);
				else if (worlds > 15 && worlds < 25)
					return "15-25";
				else if (worlds > 25 && worlds < 35)
					return "25-35";
				else
					return "35+ - Thats crazy!";
      }
    }));

    metrics.addCustomChart(new Metrics.SimplePie("worldguard_purge", new Callable<String>() {
      @Override
      public String call() {
        return getConfig().getBoolean("deletion.worldguard", true) ? "Enabled" : "Disabled";
      }
    }));

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


  public void loadLanguage() {
    File lang = new File(getDataFolder(), "language.yml");
		if (!lang.exists())
			try {
				getDataFolder().mkdir();
				lang.createNewFile();
				if (lang != null) {
					YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(lang);
					defConfig.save(lang);
					Language.setFile(defConfig);
				}
			} catch (IOException e) {
				getLogger().severe("Could not create language file!");
				Bukkit.getPluginManager().disablePlugin(this);
			}

    YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
		for (Language item : Language.values())
			if (conf.getString(item.getPath()) == null) {
				if (item.isArray()) {
					conf.set(item.getPath(), item.getDefArray());
				} else {
					conf.set(item.getPath(), item.getDefault());
				}
			}
    Language.setFile(conf);
    try {
      conf.save(lang);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}