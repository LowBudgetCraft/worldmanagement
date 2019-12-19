package crytec.worldmanagement;

import com.google.common.collect.Maps;
import crytec.worldmanagement.data.UnloadCheckTimer;
import crytec.worldmanagement.data.WorldConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class WorldManager {


  private final HashMap<String, WorldConfiguration> worldconfigs = Maps.newHashMap();
  private final WorldManagerPlugin plugin;
  private final File worldFolder;

  public WorldManager(WorldManagerPlugin instance) {
		plugin = instance;
    File f = new File(plugin.getDataFolder() + File.separator + "worlds");

		if (!f.exists() && !f.mkdir())
			instance.getLogger().warning("Failed to create plugin data directory!");

		worldFolder = f;

    loadWorldConfigurations();
		plugin.getLogger().info("All WorldConfigurations loaded, starting WorldLoad..");
  }

  /**
   * Save all worlds configurations and unload them from Bukkit.
   */
  public void shutdown() {
    for (World world : Bukkit.getWorlds()) {
			if (WorldManager.isMainWorld(world) || !hasWorldConfig(world))
				continue;

			plugin.getLogger().info("Unloading world: " + world.getName());
      saveWorldConfig(world);
      Bukkit.unloadWorld(world, true);
    }
    saveAllWorldConfigs();
  }

  public void createWorld(String folder, Environment environment, WorldType worldType) {

    World world = Bukkit.getWorld(folder);
		if (world != null)
			return;
		if (hasWorldConfig(folder))
			return;

    WorldCreator creator = new WorldCreator(folder);
    creator.environment(Environment.NORMAL);

    long seed = ThreadLocalRandom.current().nextLong();
    creator.seed(seed);
    creator.type(worldType);

		plugin.getLogger().info("Loading world [" + creator.name() + "]...");
    creator.createWorld();
    createDefaultWorldConfig(folder, environment, creator, seed);
  }

  public static void loadExistingWorld(WorldConfiguration config) {
    World world = Bukkit.getWorld(config.getWorldName());
		if (world != null)
			return;

		if (!config.isEnabled())
			return;

    WorldCreator creator = new WorldCreator(config.getWorldName());
    creator.environment(config.getEnvironment());
    creator.type(config.getWorldType());
		if (!config.getGenerator().equals("none"))
			creator.generator(config.getGenerator());

    World loadedWorld = creator.createWorld();
    loadedWorld.setDifficulty(config.getDifficulty());
    loadedWorld.setSpawnFlags(config.isMobSpawnEnabled(), config.isAnimalSpawnEnabled());
    loadedWorld.setPVP(config.isPvPEnabled());
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

		for (Chunk chunk : world.getLoadedChunks())
			chunk.unload(true);
    new UnloadCheckTimer(plugin, done, world.getName());
    Bukkit.unloadWorld(world, true);
    saveWorldConfig(world);
  }

  public void deleteWorldFolder(String worldFolderName) {
    File folder = new File(Bukkit.getWorldContainer(), worldFolderName);

		if (Bukkit.getWorld(worldFolderName) != null)
			throw new SecurityException("Denied the deletion of the world folder for name " + worldFolderName + ". The world is still in use and loaded!");
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

  public void deleteWorldConfiguration(WorldConfiguration config) {
		if (Bukkit.getWorld(config.getWorldName()) != null)
			throw new SecurityException("Denied the deletion of the world folder for name " + config.getWorldName() + ". The world is still in use and loaded!");

    String worldname = config.getWorldName();
		worldconfigs.remove(worldname);
    try {
      WorldManager.delete(new File(worldFolder, worldname + ".yml"));
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

  public WorldConfiguration getWorldConfig(World world) {
    return getWorldConfig(world.getName());
  }

  public boolean hasWorldConfig(String world) {
    return worldconfigs.containsKey(world);
  }

  public WorldConfiguration getWorldConfig(String world) {
    return worldconfigs.get(world);
  }

  public Collection<WorldConfiguration> getWorldConfigurations() {
    return worldconfigs.values();
  }


  public void startup() {
		worldconfigs.values().forEach(WorldManager::loadExistingWorld);
  }

  private void createDefaultWorldConfig(String world, Environment env, WorldCreator creator, long seed) {
    File f = new File(worldFolder, world + ".yml");
    try {
			if (!f.exists() && f.createNewFile())
				plugin.getLogger().info(world + ".yml wurde erstellt.");
    } catch (IOException e) {
      e.printStackTrace();
    }

    FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
    cfg.set("worldname", world);
    cfg.set("enabled", true);
    cfg.set("environment", creator.environment().toString());
    cfg.set("type", creator.type().toString());
    cfg.set("pvp", false);
    cfg.set("monsterspawn", true);
    cfg.set("animalspawn", true);
    cfg.set("difficulty", Difficulty.NORMAL.toString());
    cfg.set("keepspawnloaded", true);
    cfg.set("seed", seed);
    cfg.set("generator", "none");

    try {
      cfg.save(f);
      WorldConfiguration config = new WorldConfiguration(world, cfg.getString("environment"), creator.type(), cfg.getString("generator"), seed);
			worldconfigs.put(world, config);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void saveAllWorldConfigs() {

    for (WorldConfiguration config : getWorldConfigurations()) {
      File f = new File(worldFolder, config.getWorldName() + ".yml");
      YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);

      cfg.set("monsterspawn", config.isMobSpawnEnabled());
      cfg.set("animalspawn", config.isAnimalSpawnEnabled());
      cfg.set("difficulty", config.getDifficulty().toString());
      cfg.set("pvp", config.isPvPEnabled());
      cfg.set("keepspawnloaded", config.keepSpawnLoaded());
      cfg.set("enabled", config.isEnabled());
      cfg.set("seed", config.getSeed());

      try {
				plugin.getLogger().info("Saved configuration for World " + config.getWorldName() + " to disk!");
        cfg.save(f);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void saveWorldConfig(World world) {
    if (!hasWorldConfig(world)) {
			plugin.getLogger().severe("Failed to save world configuration for world " + world.getName());
      return;
    }

    WorldConfiguration config = getWorldConfig(world);
    config.setAnimalSpawn(world.getAllowAnimals());
    config.setMonsterSpawn(world.getAllowMonsters());
    config.setDifficulty(world.getDifficulty());
    config.setPvPEnabled(world.getPVP());
    config.setKeepSpawnLoaded(world.getKeepSpawnInMemory());
    config.setSeed(world.getSeed());

    File f = new File(worldFolder, world.getName() + ".yml");
    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);

    cfg.set("monsterspawn", config.isMobSpawnEnabled());
    cfg.set("animalspawn", config.isAnimalSpawnEnabled());
    cfg.set("difficulty", config.getDifficulty().toString());
    cfg.set("pvp", config.isPvPEnabled());
    cfg.set("keepspawnloaded", config.keepSpawnLoaded());
    cfg.set("enabled", config.isEnabled());
    cfg.set("seed", config.getSeed());
    cfg.set("gamemode", GameMode.SURVIVAL.toString());

    try {
      cfg.save(f);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadWorldConfigurations() {
    File worlddir = new File(plugin.getDataFolder() + File.separator + "worlds");

    for (File file : Objects.requireNonNull(worlddir.listFiles())) {
			if (!file.getName().endsWith(".yml"))
				continue;

      YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
      String worldname = cfg.getString("worldname");
      WorldType type = WorldType.valueOf(cfg.getString("type"));
      String generator = cfg.getString("generator", "none");
      long seed = cfg.getLong("seed");

      WorldConfiguration config = new WorldConfiguration(cfg.getString("worldname"), cfg.getString("environment"), type, generator, seed);

      config.setPvPEnabled(cfg.getBoolean("pvp", false));
      config.setMonsterSpawn(cfg.getBoolean("monsterspawn", true));
      config.setAnimalSpawn(cfg.getBoolean("animalspawn", true));
      config.setDifficulty(Difficulty.valueOf(cfg.getString("difficulty", Difficulty.NORMAL.toString())));
      config.setEnabled(cfg.getBoolean("enabled", true));
      config.setKeepSpawnLoaded(cfg.getBoolean("keepspawnloaded", true));
      config.setGameMode(GameMode.valueOf(cfg.getString("gamemode", GameMode.SURVIVAL.toString())));
			worldconfigs.put(worldname, config);
			plugin.getLogger().info("Loaded world config for world " + cfg.getString("worldname"));
    }
  }

  private static void delete(File file) throws IOException {
		if (file.isDirectory())
			for (File c : file.listFiles()) {
				WorldManager.delete(c);
			}
		if (!file.delete())
			throw new FileNotFoundException("Failed to delete file: " + file);
  }

}