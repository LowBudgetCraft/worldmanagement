package crytec.worldmanagement.guis;

import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

public class WorldCreationData {

  private final Player player;

  private WorldType type = WorldType.NORMAL;
  private Environment environment = Environment.NORMAL;
  private String generator = "none";
  private long seed;

  public WorldCreationData(Player player) {
    this.player = player;
    seed = ThreadLocalRandom.current().nextLong();
  }

  public void setType(WorldType type) {
    this.type = type;
  }

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  public void setGenerator(String generator) {
    this.generator = generator;
  }

  public WorldType getType() {
    return type;
  }

  public Environment getEnvironment() {
    return environment;
  }

  public String getGenerator() {
    return generator;
  }

  public void setSeed(long seed) {
    this.seed = seed;
  }

  public long getSeed() {
    return seed;
  }
}