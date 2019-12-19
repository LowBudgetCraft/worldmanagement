package crytec.worldmanagement.guis;

import crytec.worldmanagement.Language;
import crytec.worldmanagement.WorldManager;
import crytec.worldmanagement.WorldManagerPlugin;
import crytec.worldmanagement.data.WorldConfiguration;
import crytec.worldmanagement.utils.ItemBuilder;
import java.util.concurrent.ThreadLocalRandom;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

public class WorldTypeMenu implements InventoryProvider {

  public WorldTypeMenu(Environment environment, String worldname) {
    manager = WorldManagerPlugin.getInstance().getWorldManager();
    this.environment = environment;
    this.worldname = worldname;
  }

  private final WorldManager manager;
  private final Environment environment;
  private final String worldname;

  @Override
  public void init(Player player, InventoryContent content) {

    content.set(0, 1, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + WorldType.NORMAL.getName()).build(), e -> {
      player.closeInventory();
      player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
      WorldConfiguration defaultConfig = new WorldConfiguration(worldname, environment, WorldType.NORMAL, "none", ThreadLocalRandom.current().nextLong());
      Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> manager.createWorld(defaultConfig));

    }));
    content.set(0, 3, ClickableItem.of(new ItemBuilder(Material.BEDROCK).name(ChatColor.GRAY + WorldType.FLAT.getName()).build(), e -> {
      player.closeInventory();
      player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
      WorldConfiguration defaultConfig = new WorldConfiguration(worldname, environment, WorldType.FLAT, "none", ThreadLocalRandom.current().nextLong());
      Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> manager.createWorld(defaultConfig));

    }));
    content.set(0, 5, ClickableItem.of(new ItemBuilder(Material.MYCELIUM).name(ChatColor.GRAY + WorldType.LARGE_BIOMES.getName()).build(), e -> {
      player.closeInventory();
      player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
      WorldConfiguration defaultConfig = new WorldConfiguration(worldname, environment, WorldType.LARGE_BIOMES, "none", ThreadLocalRandom.current().nextLong());
      Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> manager.createWorld(defaultConfig));

    }));
    content.set(0, 7, ClickableItem.of(new ItemBuilder(Material.PHANTOM_MEMBRANE).name(ChatColor.GRAY + WorldType.AMPLIFIED.getName()).build(), e -> {
      player.closeInventory();
      player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
      WorldConfiguration defaultConfig = new WorldConfiguration(worldname, environment, WorldType.AMPLIFIED, "none", ThreadLocalRandom.current().nextLong());
      Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> manager.createWorld(defaultConfig));
    }));
  }
}