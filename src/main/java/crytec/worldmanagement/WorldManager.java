package crytec.worldmanagement;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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

import com.google.common.collect.Maps;

import crytec.worldmanagement.data.UnloadCheckTimer;
import crytec.worldmanagement.data.WorldConfiguration;
import io.netty.util.internal.ThreadLocalRandom;
import net.crytec.shaded.org.apache.commons.io.FileUtils;
import net.crytec.shaded.org.apache.lang3.EnumUtils;

public class WorldManager {

	/*
	 * Hashmap with the World as a String and a WorldConfiguration Object as the value.
	 */
	private HashMap<String, WorldConfiguration> worldconfigs = Maps.newHashMap();

	private final WorldManagerPlugin plugin;
	private final File worldFolder;

	public WorldManager(WorldManagerPlugin instance) {
		this.plugin = instance;
		File f = new File(plugin.getDataFolder() + File.separator + "worlds");
		f.mkdirs();
		this.worldFolder = f;

		this.loadWorldConfigurations();
		plugin.getLogger().info("All WorldConfigurations loaded, starting WorldLoad..");
	}

	/**
	 * Save all worlds configurations and unload them from Bukkit.
	 */
	public void shutdown() {
		for (World world : Bukkit.getWorlds()) {
			if (this.isMainWorld(world) || !this.hasWorldConfig(world))
				continue;

			plugin.getLogger().info("Unloading world: " + world.getName());
			this.saveWorldConfig(world);
			Bukkit.unloadWorld(world, true);
		}
		this.saveAllWorldConfigs();
	}

	/**
	 * Creates a new World with the given settings and creates a WorldConfiguration.
	 * 
	 * @param folder
	 * @param environment
	 * @param worldType
	 */
	public void createWorld(String folder, Environment environment, WorldType worldType) {

		World world = Bukkit.getWorld(folder);
		if (world != null) {
			return;
		}
		if (this.hasWorldConfig(folder)) {
			return;
		}

		WorldCreator creator = new WorldCreator(folder);
		creator.environment(Environment.NORMAL);

		if (EnumUtils.isValidEnum(Environment.class, environment.toString())) {
			creator.environment(Environment.valueOf(environment.toString()));
		}
		
		long seed = ThreadLocalRandom.current().nextLong();
		creator.seed(seed);
		creator.type(worldType);
		
		plugin.getLogger().info("Loading world [" + creator.name() + "]...");
		creator.createWorld();
		this.createDefaultWorldConfig(folder, environment, creator, seed);
	}

	/**
	 * Load a unloaded world via WorldConfiguration
	 * 
	 * @param config
	 */
	public void loadExistingWorld(WorldConfiguration config) {
		World world = Bukkit.getWorld(config.getWorldName());
		if (world != null) {
			return;
		}
		
		if (!config.isEnabled()) {
			return;
		}

		WorldCreator creator = new WorldCreator(config.getWorldName());
		creator.environment(config.getEnvironment());
		creator.type(config.getWorldType());
		if (!config.getGenerator().equals("none")) {
			creator.generator(config.getGenerator());
		}
		
		
		World loadedWorld = creator.createWorld();
		loadedWorld.setDifficulty(config.getDifficulty());
		loadedWorld.setSpawnFlags(config.isMobSpawnEnabled(), config.isAnimalSpawnEnabled());
		loadedWorld.setPVP(config.isPvPEnabled());
	}

	/**
	 * Unload a world from the Bukkit World list and memory
	 * and disables the world from loading again
	 * @param world
	 * @param done
	 */
	public void unloadWorld(World world, Consumer<Boolean> done) {
		if (this.isMainWorld(world)) {
			done.accept(false);
			throw new SecurityException("Denied the unloading of a world. The main world cannot be unloaded.");
		}
		this.getWorldConfig(world).setEnabled(false);
		world.setKeepSpawnInMemory(false);
		world.setAutoSave(false);
		world.getPlayers().forEach(p -> {
			p.sendMessage(Language.ERROR_WORLD_UNLOADED.toChatString());
			p.teleport( Bukkit.getWorlds().get(0).getSpawnLocation());
		});

		for (Chunk chunk : world.getLoadedChunks()) {
			chunk.unload(true);
		}
		new UnloadCheckTimer(plugin, done, world.getName());
		Bukkit.unloadWorld(world, true);
		this.saveWorldConfig(world);
	}

	/**
	 * Delete a world folder from the local disk. This action is permanent and cannot be undone!
	 * 
	 * @param worldFolderName
	 */
	public void deleteWorldFolder(String worldFolderName) {
		final File folder = new File(Bukkit.getWorldContainer(), worldFolderName);

		if (Bukkit.getWorld(worldFolderName) != null) {
			throw new SecurityException("Denied the deletion of the world folder for name " + worldFolderName + ". The world is still in use and loaded!");
		}
		try {
			FileUtils.forceDelete(folder);
			
			if (this.plugin.getConfig().getBoolean("deletion.worldguard", true)) {
				plugin.getLogger().info("Trying to remove world folder from WorldGuard...");
				
				File file = new File(Bukkit.getPluginManager().getPlugin("WorldGuard").getDataFolder(), "worlds" + File.separator + worldFolderName);
				if (file.exists()) {
					plugin.getLogger().info("Found worldguard folder at path: " + file.getAbsolutePath());
					FileUtils.forceDelete(file);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete a worldconfiguration from memory and disc. This action is permanent and cannot be undone.
	 * 
	 * @param config
	 */
	public void deleteWorldConfiguration(WorldConfiguration config) {
		if (Bukkit.getWorld(config.getWorldName()) != null) {
			throw new SecurityException("Denied the deletion of the world folder for name " + config.getWorldName() + ". The world is still in use and loaded!");
		}

		String worldname = config.getWorldName();
		this.worldconfigs.remove(worldname);
		try {
			FileUtils.forceDelete(new File(this.worldFolder, worldname + ".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isMainWorld(World world) {
		return world.getName().equals(Bukkit.getWorlds().get(0).getName());
	}

	public boolean hasWorldConfig(World world) {
		return this.hasWorldConfig(world.getName());
	}

	public WorldConfiguration getWorldConfig(World world) {
		return this.getWorldConfig(world.getName());
	}

	public boolean hasWorldConfig(String world) {
		return this.worldconfigs.containsKey(world);
	}

	public WorldConfiguration getWorldConfig(String world) {
		return this.worldconfigs.get(world);
	}
	
	public Collection<WorldConfiguration> getWorldConfigurations() {
		return this.worldconfigs.values();
	}
	
	
	public void startup() {
		this.worldconfigs.values().forEach(cfg -> this.loadExistingWorld(cfg));
	}

	private void createDefaultWorldConfig(String world, Environment env, WorldCreator creator, long seed) {
		File f = new File(this.worldFolder, world + ".yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
				plugin.getLogger().info(world + ".yml wurde erstellt.");
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			this.worldconfigs.put(world, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveAllWorldConfigs() {

		for (WorldConfiguration config : this.getWorldConfigurations()) {
			File f = new File(this.worldFolder, config.getWorldName() + ".yml");
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
		if (!this.hasWorldConfig(world)) {
			plugin.getLogger().severe("Failed to save world configuration for world " + world.getName());
			return;
		}

		WorldConfiguration config = this.getWorldConfig(world);
		config.setAnimalSpawn(world.getAllowAnimals());
		config.setMonsterSpawn(world.getAllowMonsters());
		config.setDifficulty(world.getDifficulty());
		config.setPvPEnabled(world.getPVP());
		config.setKeepSpawnLoaded(world.getKeepSpawnInMemory());
		config.setSeed(world.getSeed());

		File f = new File(this.worldFolder, world.getName() + ".yml");
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
		String[] ext = { "yml" };
		File worlddir = new File(plugin.getDataFolder() + File.separator + "worlds");
		Iterator<File> worlds = FileUtils.iterateFiles(worlddir, ext, false);

		while (worlds.hasNext()) {
			File file = worlds.next();
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
			this.worldconfigs.put(worldname, config);
			plugin.getLogger().info("Loaded world config for world " + cfg.getString("worldname"));
		}
	}
}