package crytec.worldmanagement;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.world.WorldLoadEvent;

import crytec.worldmanagement.data.WorldConfiguration;

public class WorldListener implements Listener {

	private final WorldManagerPlugin plugin;

	public WorldListener(WorldManagerPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void forceGameMode(PlayerChangedWorldEvent event) {
		if (plugin.getWorldManager().hasWorldConfig(event.getPlayer().getWorld())) {
			GameMode mode = plugin.getWorldManager().getWorldConfig(event.getPlayer().getWorld()).getForcedGameMode();
			
			if (event.getPlayer().getGameMode() != mode) {
				if (event.getPlayer().hasPermission("worldmanagement.gamemode.bypass")) {
					event.getPlayer().sendMessage(Language.GENERAL_GAMEMODE_FORCED_BYPASS.toChatString());
					return;
				}
				event.getPlayer().setGameMode(mode);
				event.getPlayer().sendMessage(Language.GENERAL_GAMEMODE_FORCED.toChatString().replace("%gamemode%", mode.toString()));
			}
		}
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent e) {
		if (!plugin.getWorldManager().hasWorldConfig(e.getWorld()))
			return;
		WorldConfiguration worldconfig = plugin.getWorldManager().getWorldConfig(e.getWorld());

		e.getWorld().setPVP(worldconfig.isPvPEnabled());
		e.getWorld().setDifficulty(worldconfig.getDifficulty());
		e.getWorld().setSpawnFlags(worldconfig.isMobSpawnEnabled(), worldconfig.isAnimalSpawnEnabled());
		e.getWorld().setKeepSpawnInMemory(worldconfig.keepSpawnLoaded());

		plugin.getLogger().info("Loaded Worldconfiguration for world " + e.getWorld().getName());
		plugin.getLogger().info("PvP: " + e.getWorld().getPVP());
		plugin.getLogger().info("Monsterspawn: " + e.getWorld().getAllowMonsters());
		plugin.getLogger().info("Monsterspawn: " + e.getWorld().getAllowAnimals());
		plugin.getLogger().info("Difficulty: " + e.getWorld().getDifficulty().name());
		plugin.getLogger().info("Keep Spawn Loaded: " + e.getWorld().getKeepSpawnInMemory());
		plugin.getLogger().info("Environment: " + e.getWorld().getEnvironment());
		plugin.getLogger().info("WorldType: " + e.getWorld().getWorldType());

	}
}
