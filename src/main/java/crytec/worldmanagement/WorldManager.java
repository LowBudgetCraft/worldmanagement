package crytec.worldmanagement;

import com.google.common.collect.Maps;
import crytec.worldmanagement.data.UnloadCheckTimer;
import crytec.worldmanagement.data.WorldConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;

public class WorldManager {


  private final HashMap<String, WorldConfiguration> worldconfigs = Maps.newHashMap();
  private final WorldManagerPlugin plugin;
  private final File worldFolder;

  public WorldManager(WorldManagerPlugin instance) {
    plugin = instance;
    File f = new File(plugin.getDataFolder() + File.separator + "worlds");

    if (!f.exists() && !f.mkdir()) {
      instance.getLogger().warning("Failed to create plugin data directory!");
    }

    worldFolder = f;

    loadWorldConfigurations();
    plugin.getLogger().info("All WorldConfigurations loaded, starting WorldLoad..");
  }

  public void initialize() {
    worldconfigs.values().forEach(this::createWorld);
  }

  /**
   * Save all worlds configurations and unload them from Bukkit.
   */
  public void shutdown() {
    for (World world : Bukkit.getWorlds()) {
      if (WorldManager.isMainWorld(world) || !hasWorldConfig(world)) {
        continue;
      }

      plugin.getLogger().info("Unloading world: " + world.getName());
      Bukkit.unloadWorld(world, true);
    }
    try {
      saveWorldConfig();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void createWorld(WorldConfiguration worldConfiguration) {
    if (Bukkit.getWorld(worldConfiguration.getWorldName()) != null) {
      plugin.getLogger().severe("Failed to create a new world - World " + worldConfiguration.getWorldName() + " already exists");
      return;
    }

    if (!worldConfiguration.isEnabled()) {
      return;
    }

    WorldCreator creator = new WorldCreator(worldConfiguration.getWorldName());
    creator.environment(worldConfiguration.getEnvironment());
    creator.type(worldConfiguration.getWorldType());
    creator.seed(worldConfiguration.getSeed());

    if (worldConfiguration.getGenerator() != null && !worldConfiguration.getGenerator().equals("none")) {
      creator.generator(worldConfiguration.getGenerator());
    }

    World world = creator.createWorld();
    plugin.getLogger().info("Loading world [" + creator.name() + "] with seed " + creator.seed());
    worldconfigs.put(world.getName(), worldConfiguration);
    worldConfiguration.setBukkitWorld(world);

    world.setDifficulty(worldConfiguration.getDifficulty());
    world.setSpawnFlags(worldConfiguration.isMobSpawnEnabled(), worldConfiguration.isAnimalSpawnEnabled());
    world.setKeepSpawnInMemory(worldConfiguration.keepSpawnLoaded());
    world.setPVP(worldConfiguration.isPvPEnabled());
  }

  public void unloadWorld(World world, Consumer<Boolean> done) {
    if (WorldManager.isMainWorld(world)) {
      done.accept(false);
      throw new SecurityException("Denied the unloading of a world. The main world cannot be unloaded.");
    }
    getWorldConfig(world).setEnabled(false);
    world.setKeepSpawnInMemory(false);
    world.setAutoSave(false);
    world.getPlayers().forEach(p -> {
      p.sendMessage(Language.ERROR_WORLD_UNLOADED.toChatString());
      p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
    });

    for (Chunk chunk : world.getLoadedChunks()) {
      chunk.unload(true);
    }
    new UnloadCheckTimer(plugin, done, world.getName());
    Bukkit.unloadWorld(world, true);
    try {
      saveWorldConfig();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void deleteWorldConfiguration(String worldname) {
    worldconfigs.remove(worldname);
    try {
      saveWorldConfig();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void deleteWorldFolder(String worldFolderName) {
    File folder = new File(Bukkit.getWorldContainer(), worldFolderName);

    if (Bukkit.getWorld(worldFolderName) != null) {
      throw new SecurityException("Denied the deletion of the world folder for name " + worldFolderName + ". The world is still in use and loaded!");
    }
    try {
      WorldManager.delete(folder);

      if (plugin.getConfig().getBoolean("deletion.worldguard", true)) {
        plugin.getLogger().info("Trying to remove world folder from WorldGuard...");

        File file = new File(Bukkit.getPluginManager().getPlugin("WorldGuard").getDataFolder(), "worlds" + File.separator + worldFolderName);
        if (file.exists()) {
          plugin.getLogger().info("Found worldguard folder at path: " + file.getAbsolutePath());
          WorldManager.delete(file);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean isMainWorld(World world) {
    return world.getName().equals(Bukkit.getWorlds().get(0).getName());
  }

  public boolean hasWorldConfig(World world) {
    return hasWorldConfig(world.getName());
  }

  public boolean hasWorldConfig(String world) {
    return worldconfigs.containsKey(world);
  }

  public WorldConfiguration getWorldConfig(World world) {
    return getWorldConfig(world.getName());
  }

  public WorldConfiguration getWorldConfig(String world) {
    return worldconfigs.get(world);
  }

  public Collection<WorldConfiguration> getWorldConfigurations() {
    return worldconfigs.values();
  }

  private void saveWorldConfig() throws IOException {
    if (worldconfigs.isEmpty()) {
      return;
    }

    File configFile = new File(plugin.getDataFolder(), "worlds.yml");
    if (!configFile.exists()) {
      configFile.createNewFile();
    }

    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);

    for (String key : cfg.getRoot().getKeys(false)) {
      if (!worldconfigs.containsKey(key)) {
        plugin.getLogger().info("Found outdated configuration entry - removing " + key);
        cfg.set(key, null);
      }
    }

    for (WorldConfiguration wcfg : worldconfigs.values()) {
      cfg.set(wcfg.getWorldName(), wcfg);
    }

    cfg.save(configFile);
  }

  private void loadWorldConfigurations() {
    File configFile = new File(plugin.getDataFolder(), "worlds.yml");
    if (!configFile.exists()) {
      return;
    }

    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);

    for (String worldEntry : cfg.getRoot().getKeys(false)) {
      WorldConfiguration worldConfig = cfg.getSerializable(worldEntry, WorldConfiguration.class);
      if (worldConfig == null) {
        plugin.getLogger().severe("Failed to load WorldConfiguration for entry: " + worldEntry);
        continue;
      }

      worldconfigs.put(worldConfig.getWorldName(), worldConfig);
      plugin.getLogger().info("Loaded world config for world " + worldConfig.getWorldName());
    }
  }

  private static void delete(File file) throws IOException {
    if (file.isDirectory()) {
      for (File c : file.listFiles()) {
        WorldManager.delete(c);
      }
    }
    if (!file.delete()) {
      throw new FileNotFoundException("Failed to delete file: " + file);
    }
  }
}