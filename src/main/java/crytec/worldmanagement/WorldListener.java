/*
 *
 *  * This file is part of WorldManagement, licensed under the MIT License.
 *  *
 *  *  Copyright (c) crysis992 <crysis992@gmail.com>
 *  *  Copyright (c) contributors
 *  *
 *  *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  *  of this software and associated documentation files (the "Software"), to deal
 *  *  in the Software without restriction, including without limitation the rights
 *  *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  *  copies of the Software, and to permit persons to whom the Software is
 *  *  furnished to do so, subject to the following conditions:
 *  *
 *  *  The above copyright notice and this permission notice shall be included in all
 *  *  copies or substantial portions of the Software.
 *  *
 *  *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  *  SOFTWARE.
 *
 */

package crytec.worldmanagement;

import crytec.worldmanagement.data.WorldConfiguration;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldListener implements Listener {

  private final WorldManagerPlugin plugin;
  private final WorldManager manager;

  public WorldListener(WorldManagerPlugin plugin, WorldManager manager) {
    this.plugin = plugin;
    this.manager = manager;
  }

  @EventHandler
  public void checkWorldPermission(PlayerTeleportEvent event) {
    World world = event.getTo().getWorld();
    if (!manager.hasWorldConfig(world)) {
      return;
    }
    WorldConfiguration config = manager.getWorldConfig(world);
    if (!config.hasPermission()) {
      return;
    }

    if (!event.getPlayer().hasPermission(config.getPermission()) && !event.getPlayer().hasPermission("worldmanagement.permission.bypass")) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(Language.ERROR_NO_WORLD_PERM.toChatString());
    }
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
