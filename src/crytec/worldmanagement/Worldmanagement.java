package crytec.worldmanagement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import crytec.worldmanagement.data.WorldConfiguration;
import net.crytec.api.util.F;

public class Worldmanagement extends JavaPlugin implements Listener {

	public final Logger log = Bukkit.getLogger();
	public final String prefix = "[WorldManagement] ";
	private static Worldmanagement instance;

	private HashMap<String, WorldConfiguration> worldconfigs = new HashMap<String, WorldConfiguration>();

	@Override
	public void onLoad() {
		Worldmanagement.instance = this;
		File f = new File(getDataFolder() + File.separator + "worlds");
		f.mkdirs();
	}

	@Override
	public void onEnable() {
		this.loadWorldConfigurations();
		Bukkit.getPluginManager().registerEvents(this, this);
		this.getCommand("worldmanagement").setExecutor(new WorldCommands());
		initializeWorlds();
	}
	
	@Override
	public void onDisable() {
		for (World world : Bukkit.getWorlds()) {
			this.saveWorldConfig(world);
		}
	}

	public static Worldmanagement getInstance() {
		return instance;
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent e) {
		if (this.worldconfigs.containsKey(e.getWorld().getName())) {

			WorldConfiguration worldconfig = this.worldconfigs.get(e.getWorld().getName());

			e.getWorld().setPVP(worldconfig.isPvPEnabled());
			e.getWorld().setDifficulty(worldconfig.getDifficulty());
			e.getWorld().setSpawnFlags(worldconfig.isMobSpawnEnabled(), worldconfig.isAnimalSpawnEnabled());
			e.getWorld().setKeepSpawnInMemory(worldconfig.keepSpawnLoaded());

			this.log.info("Loaded Worldconfiguration.");
			this.log.info("PvP: " + e.getWorld().getPVP());
			this.log.info("Monsterspawn: " + e.getWorld().getAllowMonsters());
			this.log.info("Monsterspawn: " + e.getWorld().getAllowAnimals());
			this.log.info("Difficulty: " + e.getWorld().getDifficulty().name());
			this.log.info("Keep Spawn Loaded: " + e.getWorld().getKeepSpawnInMemory());

		}
	}

	/* UTIL METHODS */

	public void deleteWorld(World world, Consumer<Boolean> status) {
		long start = System.currentTimeMillis();
		if (world == null) {
			return;
		}

		String worldname = world.getName();

		world.setKeepSpawnInMemory(false);
		world.setAutoSave(false);

		final File folder = world.getWorldFolder();

		this.unloadWorld(world.getName());

		try {
			FileUtils.forceDelete(folder);
			log.info(prefix + "Deletion took " + (System.currentTimeMillis() - start) + "ms");
		} catch (IOException ex) {
			ex.printStackTrace();
			status.accept(false);
		}
		File f = new File(getDataFolder() + File.separator + "worlds" + File.separator + worldname + ".yml");
		f.delete();
		this.worldconfigs.remove(worldname);
		log.info(prefix + "Deleted world configuration [" + worldname + "]!");
		status.accept(true);

	}

	/**
	 * Remove a world from the configuration list but do not delete the entire
	 * worldfolder! Also unloads the world and teleports all players to the
	 * mainworld spawn point.
	 * 
	 * @param world
	 * @param status
	 */
	public void removeWorld(World world, Consumer<Boolean> status) {
		if (world == null) {
			return;
		}

		String worldname = world.getName();

		this.unloadWorld(world.getName());

		File f = new File(getDataFolder() + File.separator + "worlds" + File.separator + worldname + ".yml");
		f.delete();
		this.worldconfigs.remove(worldname);
		log.info(prefix + "Deleted world configuration [" + worldname + "]!");
		status.accept(true);
	}

	public boolean doesWorldexsists(String world) {
		File check = new File(Bukkit.getWorldContainer() + File.separator + world);
		if (check.exists()) {
			return false;
		}
		return true;
	}

	public Environment getEnvironmentFromString(String environment) {
		String env = environment.toUpperCase().replaceAll("end", "THE_END");
		try {
			return Environment.valueOf(env);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	public WorldType getWorldTypeFromString(String worldtype) {
		try {
			return WorldType.valueOf(worldtype.toUpperCase());
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	public boolean createNewWorld(String name, Environment enviroment, WorldType type, String generator) {
		loadWorld(name, enviroment, type, generator);
		File f = new File(getDataFolder() + File.separator + "worlds" + File.separator + name + ".yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
				log.info(prefix + name + ".yml wurde erstellt.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		cfg.set("worldname", name);
		cfg.set("enabled", true);
		cfg.set("environment", enviroment.name());
		cfg.set("type", type.name());
		cfg.set("pvp", false);
		cfg.set("monsterspawn", true);
		cfg.set("animalspawn", true);
		cfg.set("difficulty", Difficulty.NORMAL.toString());
		cfg.set("keepspawnloaded", true);
		if (generator == null) {
			cfg.set("generator", "none");
		} else {
			cfg.set("generator", generator);
		}
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.loadWorldConfigurations();
		return true;
	}

	public boolean unloadWorld(String world) {
		if (doesWorldexsists(world)) {
			log.warning(F.error("Die angegebene Welt existiert nicht!"));
			return false;
		}

		if (Bukkit.getWorld(world) == Bukkit.getWorlds().get(0)) {
			log.warning(F.error("Du kannst die Standardwelt nicht deaktivieren."));
			return false;
		}

		for (Player p : Bukkit.getWorld(world).getPlayers()) {
			p.sendMessage(F.main("WorldManagement", "Du wurdest an den Spawn teleportiert!"));
			p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
		}

		for (Chunk c : Bukkit.getWorld(world).getLoadedChunks()) {
			c.unload(true);
		}
		Bukkit.unloadWorld(world, true);
		return true;
	}

	public void loadWorld(String folder, Environment enviroment) {
		loadWorld(folder, enviroment, WorldType.NORMAL, "none");
	}

	public void loadWorld(String folder, Environment enviroment, WorldType type) {
		loadWorld(folder, enviroment, type, "none");
	}

	/**
	 * Load a specific world
	 * 
	 * @param folder
	 * @param enviroment
	 * @param type
	 * @param generator
	 */
	public void loadWorld(String folder, Environment enviroment, WorldType type, String generator) {
		String gen = generator;

		World w = Bukkit.getWorld(folder);
		if (w != null) {
			return;
		}

		WorldCreator wc = new WorldCreator(folder);

		if (gen == null) {
			gen = "none";
			log.info("No Generator Set.");
		}

		if (generator.equalsIgnoreCase("void")) {
			wc.generator(new VoidWorld());
		}

		else if (gen.equalsIgnoreCase("none")) {
			log.info("No Generator Set.");

		} else {
			wc.generator(gen);
		}
		wc.environment(enviroment);
		wc.generateStructures(true);
		wc.type(type);
		Bukkit.createWorld(wc);
		log.info(prefix + "Welt [" + folder + "] wurde geladen.");
	}

	public WorldConfiguration getWorldConfig(World world) {
		return getWorldConfig(world.getName());
	}

	public WorldConfiguration getWorldConfig(String worldname) {
		return this.worldconfigs.getOrDefault(worldname, null);
	}

	private void loadWorldConfigurations() {
		String[] ext = { "yml" };
		File worlddir = new File(getDataFolder() + File.separator + "worlds");
		Iterator<File> worlds = FileUtils.iterateFiles(worlddir, ext, false);

		while (worlds.hasNext()) {
			try {
				File file = worlds.next();
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				String worldname = cfg.getString("worldname");
				WorldType type = WorldType.valueOf(cfg.getString("type"));
				String generator = "none";

				if (cfg.isSet("generator")) {
					generator = cfg.getString("generator");
				}

				WorldConfiguration config = new WorldConfiguration(cfg.getString("worldname"), cfg.getString("environment"), type, generator);

				if (cfg.isSet("pvp")) {
					config.setPvPEnabled(cfg.getBoolean("pvp"));
				} else {
					cfg.set("pvp", false);
					cfg.save(file);
				}

				if (cfg.isSet("monsterspawn")) {
					config.setMonsterSpawn(cfg.getBoolean("monsterspawn"));
				} else {
					cfg.set("monsterspawn", true);
					cfg.save(file);
				}

				if (cfg.isSet("animalspawn")) {
					config.setAnimalSpawn(cfg.getBoolean("animalspawn"));
				} else {
					cfg.set("animalspawn", true);
					cfg.save(file);
				}

				if (cfg.isSet("difficulty")) {
					config.setDifficulty(Difficulty.valueOf(cfg.getString("difficulty")));
				} else {
					cfg.set("difficulty", Difficulty.NORMAL.toString());
					cfg.save(file);
				}
				
				if (cfg.isSet("enabled")) {
					config.setEnabled(cfg.getBoolean("enabled"));
				} else {
					cfg.set("enabled", true);
					cfg.save(file);
				}
				
				if (cfg.isSet("keepspawnloaded")) {
					config.setKeepSpawnLoaded(cfg.getBoolean("keepspawnloaded"));
				} else {
					cfg.set("keepspawnloaded", true);
					cfg.save(file);
				}
				this.worldconfigs.put(worldname, config);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

	private void initializeWorlds() {
		for (WorldConfiguration cfg : this.worldconfigs.values()) {
			if (cfg.isEnabled()) {
				this.loadWorld(cfg.getWorldName(), cfg.getEnvironment(), cfg.getWorldType(), cfg.getGenerator());
			}
		}
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		this.loadWorldConfigurations();
		for (World world : Bukkit.getWorlds()) {
			if (this.worldconfigs.containsKey(world.getName())) {
				WorldConfiguration worldconfig = this.worldconfigs.get(world.getName());
				world.setPVP(worldconfig.isPvPEnabled());
				world.setDifficulty(worldconfig.getDifficulty());
				world.setSpawnFlags(worldconfig.isMobSpawnEnabled(), worldconfig.isAnimalSpawnEnabled());

				this.log.info("Loaded Worldconfiguration for World: " + world.getName());
				this.log.info("PvP: " + world.getPVP());
				this.log.info("Monsterspawn: " + world.getAllowMonsters());
				this.log.info("Monsterspawn: " + world.getAllowAnimals());
				this.log.info("Difficulty: " + world.getDifficulty().name());
			}

		}
	}
	
	public void saveWorldConfig(World world) {
		WorldConfiguration config = this.getWorldConfig(world);
		if (config == null) return;
		
		config.setAnimalSpawn(world.getAllowAnimals());
		config.setMonsterSpawn(world.getAllowMonsters());
		config.setDifficulty(world.getDifficulty());
		config.setPvPEnabled(world.getPVP());
		config.setKeepSpawnLoaded(world.getKeepSpawnInMemory());
		
		File f = new File(getDataFolder() + File.separator + "worlds" + File.separator + world.getName() + ".yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		
		cfg.set("monsterspawn", config.isMobSpawnEnabled());
		cfg.set("animalspawn", config.isAnimalSpawnEnabled());
		cfg.set("difficulty", config.getDifficulty().toString());
		cfg.set("pvp", config.isPvPEnabled());
		cfg.set("keepspawnloaded", config.keepSpawnLoaded());
		cfg.set("enabled", config.isEnabled());
		
		try {
			this.log.info("Saved configuration for World " + world.getName() + " to disk!");
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
