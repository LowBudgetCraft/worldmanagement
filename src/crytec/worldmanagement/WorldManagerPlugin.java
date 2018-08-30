package crytec.worldmanagement;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

public class WorldManagerPlugin extends JavaPlugin implements Listener {

	private static WorldManagerPlugin instance;

	@Getter
	private WorldManager worldManager;

	@Override
	public void onLoad() {
		WorldManagerPlugin.instance = this;
		this.saveResource("config.yml", false);
	}

	@Override
	public void onEnable() {
		if (this.getConfig().getBoolean("verbose", true)) {
		Bukkit.getPluginManager().registerEvents(new WorldListener(this), this);
		}
		this.worldManager = new WorldManager(this);
		this.getCommand("worldmanagement").setExecutor(new WorldCommands());
		this.worldManager.startup();
	}

	@Override
	public void onDisable() {
		this.getWorldManager().shutdown();
	}

	public static WorldManagerPlugin getInstance() {
		return instance;
	}
}