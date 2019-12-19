package crytec.worldmanagement.data;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;

public class WorldConfiguration {


  private final String name;
  private final Environment environment;
  private final WorldType type;
  private final String generator;
  private long seed;

  private boolean pvp = false;
  private boolean monsterspawn = true;
  private boolean animalspawn = true;
  private Difficulty difficulty = Difficulty.NORMAL;
  private boolean keepSpawnLoaded = true;
  private GameMode gamemode = GameMode.SURVIVAL;

  private boolean enabled = true;


  public WorldConfiguration(String world, String environment, WorldType type, String generator, long seed) {
		name = world;
    this.environment = Environment.valueOf(environment);
    this.type = type;
    this.generator = generator;
    this.seed = seed;
  }


  public boolean hasWorldGenerator() {
    return !generator.equals("none");
  }

  public String getWorldName() {
    return name;
  }

  public Environment getEnvironment() {
    return environment;
  }

  public String getGenerator() {
    return generator;
  }

  public WorldType getWorldType() {
    return type;
  }

  public boolean isPvPEnabled() {
    return pvp;
  }

  public boolean isMobSpawnEnabled() {
    return monsterspawn;
  }

  public boolean isAnimalSpawnEnabled() {
    return animalspawn;
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }

  public boolean keepSpawnLoaded() {
    return keepSpawnLoaded;
  }


  public void setPvPEnabled(boolean status) {
		pvp = status;
  }

  public void setDifficulty(Difficulty difficulty) {
    this.difficulty = difficulty;
  }

  public void setMonsterSpawn(boolean allow) {
		monsterspawn = allow;
  }

  public void setAnimalSpawn(boolean allow) {
		animalspawn = allow;
  }

  public void setKeepSpawnLoaded(boolean value) {
		keepSpawnLoaded = value;
  }

  public void setSeed(long seed) {
    this.seed = seed;
  }

  public long getSeed() {
    return seed;
  }

  public void setGameMode(GameMode gamemode) {
    this.gamemode = gamemode;
  }

  public GameMode getForcedGameMode() {
    return gamemode;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }

}