package crytec.worldmanagement.data;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class WorldConfiguration implements ConfigurationSerializable {


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
  private String permission = "";

  private boolean enabled = true;

  private WeakReference<World> bukkitWorld;


  public WorldConfiguration(String world, Environment environment, WorldType type, String generator, long seed) {
    name = world;
    this.environment = environment;
    this.type = type;
    this.generator = generator;
    this.seed = seed;
  }

  public WorldConfiguration(Map<String, Object> serializedData) {

    name = (String) serializedData.get("worldname");
    environment = Environment.valueOf((String) serializedData.get("environment"));
    type = WorldType.valueOf((String) serializedData.get("worldtype"));
    generator = (String) serializedData.get("generator");
    seed = (long) serializedData.get("seed");
    gamemode = GameMode.valueOf((String) serializedData.get("gamemode"));
    difficulty = Difficulty.valueOf((String) serializedData.get("difficulty"));
    enabled = (boolean) serializedData.get("enabled");

    pvp = (boolean) serializedData.get("options.pvp");
    monsterspawn = (boolean) serializedData.get("options.monsterspawn");
    animalspawn = (boolean) serializedData.get("options.animalspawn");
    keepSpawnLoaded = (boolean) serializedData.get("options.keepspawnloaded");
    permission = (String) serializedData.get("permission");
  }

  public void setBukkitWorld(World world) {
    bukkitWorld = new WeakReference<>(world);
  }


  public boolean hasWorldGenerator() {
    return !generator.equals("none");
  }

  @NotNull
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


  public void setPvp(boolean pvp) {
    if (bukkitWorld != null && bukkitWorld.get() != null) {
      bukkitWorld.get().setPVP(pvp);
    }
    this.pvp = pvp;
  }

  public void setDifficulty(Difficulty difficulty) {
    if (bukkitWorld != null && bukkitWorld.get() != null) {
      bukkitWorld.get().setDifficulty(difficulty);
    }
    this.difficulty = difficulty;
  }

  public void setMonsterspawn(boolean monsterspawn) {
    if (bukkitWorld != null && bukkitWorld.get() != null) {
      bukkitWorld.get().setSpawnFlags(monsterspawn, bukkitWorld.get().getAllowAnimals());
    }

    this.monsterspawn = monsterspawn;
  }

  public void setAnimalspawn(boolean animalspawn) {
    if (bukkitWorld != null && bukkitWorld.get() != null) {
      bukkitWorld.get().setSpawnFlags(bukkitWorld.get().getAllowMonsters(), animalspawn);
    }
    this.animalspawn = animalspawn;
  }

  public void setKeepSpawnLoaded(boolean keepSpawnLoaded) {
    if (bukkitWorld != null && bukkitWorld.get() != null) {
      bukkitWorld.get().setKeepSpawnInMemory(keepSpawnLoaded);
    }
    this.keepSpawnLoaded = keepSpawnLoaded;
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

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public boolean hasPermission() {
    return permission != null && !permission.isEmpty() && !permission.equals("none");
  }

  @Override
  public int hashCode() {
    return Objects.hash(environment, seed, type, name);
  }

  @Override
  public String toString() {
    return "WorldData [world=" + name + ", seed=" + seed + ", environment=" + environment + ", type=" + type
        + ", generator=" + generator + ", pvp=" + pvp + ", monsterspawn=" + monsterspawn + ", animalspawn=" + animalspawn
        + ", difficulty=" + difficulty + ", keepSpawnLoaded=" + keepSpawnLoaded + ", gamemode=" + gamemode + ", forceGameMode="
        + getForcedGameMode() + ", enabled=" + enabled + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof WorldConfiguration)) {
      return false;
    }
    WorldConfiguration other = (WorldConfiguration) obj;
    return environment == other.environment && seed == other.seed && type == other.type && Objects.equals(getWorldName(), other.getWorldName());
  }

  @NotNull
  @Override
  public Map<String, Object> serialize() {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();

    map.put("worldname", getWorldName());
    map.put("environment", getEnvironment().toString());
    map.put("worldtype", getWorldType().toString());
    map.put("generator", getGenerator());
    map.put("seed", getSeed());
    map.put("gamemode", getForcedGameMode().toString());
    map.put("difficulty", getDifficulty().toString());

    map.put("enabled", isEnabled());
    map.put("permission", permission);

    map.put("options.pvp", isPvPEnabled());
    map.put("options.monsterspawn", monsterspawn);
    map.put("options.animalspawn", animalspawn);
    map.put("options.keepspawnloaded", keepSpawnLoaded());

    return map;
  }


}