package crytec.worldmanagement;

import crytec.worldmanagement.data.WorldConfiguration;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldListener implements Listener {

  private final WorldManagerPlugin plugin;
  private final WorldManager manager;

  public WorldListener(WorldManagerPlugin plugin, WorldManager manager) {
    this.plugin = plugin;
    this.manager = manager;
  }

  @EventHandler(ignoreCancelled = true)
  public void forceGameMode(PlayerChangedWorldEvent event) {
    if (manager.hasWorldConfig(event.getPlayer().getWorld())) {
      GameMode mode = manager.getWorldConfig(event.getPlayer().getWorld()).getForcedGameMode();

      if (event.getPlayer().getGameMode() != mode) {
        if (event.getPlayer().hasPermission("worldmanagement.gamemode.bypass")) {
          return;
        }
        event.getPlayer().setGameMode(mode);
        event.getPlayer().sendMessage(Language.GENERAL_GAMEMODE_FORCED.toChatString().replace("%gamemode%", mode.toString()));
      }
    }
  }

  @EventHandler
  public void onWorldLoad(WorldLoadEvent e) {
    if (!manager.hasWorldConfig(e.getWorld())) {
      return;
    }
    WorldConfiguration worldconfig = manager.getWorldConfig(e.getWorld());

    e.getWorld().setPVP(worldconfig.isPvPEnabled());
    e.getWorld().setDifficulty(worldconfig.getDifficulty());
    e.getWorld().setSpawnFlags(worldconfig.isMobSpawnEnabled(), worldconfig.isAnimalSpawnEnabled());
    e.getWorld().setKeepSpawnInMemory(worldconfig.keepSpawnLoaded());

    if (plugin.getConfig().getBoolean("verbose", true)) {
      plugin.getLogger().info("Loaded Worldconfiguration for world " + e.getWorld().getName());
      plugin.getLogger().info("PvP: " + e.getWorld().getPVP());
      plugin.getLogger().info("Monsterspawn: " + e.getWorld().getAllowMonsters());
      plugin.getLogger().info("Animalspawn: " + e.getWorld().getAllowAnimals());
      plugin.getLogger().info("Difficulty: " + e.getWorld().getDifficulty().name());
      plugin.getLogger().info("Keep Spawn Loaded: " + e.getWorld().getKeepSpawnInMemory());
      plugin.getLogger().info("Environment: " + e.getWorld().getEnvironment());
      plugin.getLogger().info("WorldType: " + e.getWorld().getWorldType());
    }
  }
}
