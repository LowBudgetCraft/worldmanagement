package crytec.worldmanagement.data;

import org.bukkit.Difficulty;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;

public class WorldConfiguration {
	
	
	private String name;
	private Environment environment;
	private WorldType type;
	private String generator;
	
	private boolean pvp = false;
	private boolean monsterspawn = true;
	private boolean animalspawn = true;
	private Difficulty difficulty = Difficulty.NORMAL;
	private boolean keepSpawnLoaded = true;
	
	
	public WorldConfiguration(String world, String environment, WorldType type, String generator) {
		this.name = world;
		this.environment = Environment.valueOf(environment);
		this.type = type;
		this.generator = generator;
	}
	
	
	public boolean hasWorldGenerator() {
		if (generator.equals("none")) return false;
		else return true;
	}
	
	public String getWorldName() {
		return this.name;
	}
	
	public Environment getEnvironment() {
		return this.environment;
	}
	
	public String getGenerator() {
		return this.generator;
	}
	
	public WorldType getWorldType() {
		return this.type;
	}
	
	public boolean isPvPEnabled() {
		return this.pvp;
	}
	
	public boolean isMobSpawnEnabled() {
		return this.monsterspawn;
	}
	
	public boolean isAnimalSpawnEnabled() {
		return this.animalspawn;
	}
	
	public Difficulty getDifficulty() {
		return this.difficulty;
	}
	
	public boolean keepSpawnLoaded() {
		return this.keepSpawnLoaded;
	}
	
	
	public void setPvPEnabled(boolean status) {
		this.pvp = status;
	}
	
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}
	
	public void setMonsterSpawn(boolean allow) {
		this.monsterspawn = allow;
	}
	
	public void setAnimalSpawn(boolean allow) {
		this.animalspawn = allow;
	}
	
	public void setKeepSpawnLoaded(boolean value) {
		this.keepSpawnLoaded = value;
	}

}
