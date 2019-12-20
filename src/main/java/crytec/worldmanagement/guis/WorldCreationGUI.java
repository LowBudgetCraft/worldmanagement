package crytec.worldmanagement.guis;

import crytec.worldmanagement.Language;
import crytec.worldmanagement.WorldManagerPlugin;
import crytec.worldmanagement.data.WorldConfiguration;
import crytec.worldmanagement.utils.ItemBuilder;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.libs.commons.utils.chatinput.ChatInput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemFlag;

public class WorldCreationGUI implements InventoryProvider {

  private final WorldCreationData data;
  private final String worldname;

  public WorldCreationGUI(Player player, String worldname) {
    data = new WorldCreationData(player);
    this.worldname = worldname;
  }

  @Override
  public void init(Player player, InventoryContent content) {

    if (data.getEnvironment() == Environment.NORMAL) {
      content.set(0, 3, ClickableItem.of(new ItemBuilder(Material.GRASS).enchantment(Enchantment.ARROW_DAMAGE).setItemFlag(ItemFlag.HIDE_ENCHANTS)
          .name(Language.ENVIRONMENT_NORMAL.toString()).build(), e -> {
        data.setEnvironment(Environment.NORMAL);
        reopen(player, content);
      }));
    } else {
      content.set(0, 3, ClickableItem.of(new ItemBuilder(Material.GRASS).name(Language.ENVIRONMENT_NORMAL.toString()).build(), e -> {
        data.setEnvironment(Environment.NORMAL);
        reopen(player, content);
      }));
    }

    if (data.getEnvironment() == Environment.NETHER) {
      content.set(0, 5, ClickableItem.of(new ItemBuilder(Material.NETHER_WART).enchantment(Enchantment.ARROW_DAMAGE).setItemFlag(ItemFlag.HIDE_ENCHANTS)
          .name(Language.ENVIRONMENT_NETHER.toString()).build(), e -> {
        data.setEnvironment(Environment.NETHER);
        reopen(player, content);
      }));
    } else {
      content.set(0, 5, ClickableItem.of(new ItemBuilder(Material.NETHER_WART).name(Language.ENVIRONMENT_NETHER.toString()).build(), e -> {
        data.setEnvironment(Environment.NETHER);
        reopen(player, content);
      }));
    }

    if (data.getEnvironment() == Environment.THE_END) {
      content.set(0, 7, ClickableItem.of(new ItemBuilder(Material.END_CRYSTAL).enchantment(Enchantment.ARROW_DAMAGE).setItemFlag(ItemFlag.HIDE_ENCHANTS)
          .name(Language.ENVIRONMENT_END.toString()).build(), e -> {
        data.setEnvironment(Environment.THE_END);
        reopen(player, content);
      }));
    } else {
      content.set(0, 7, ClickableItem.of(new ItemBuilder(Material.END_CRYSTAL).name(Language.ENVIRONMENT_END.toString()).build(), e -> {
        data.setEnvironment(Environment.THE_END);
        reopen(player, content);
      }));
    }


    /*
    WorldType selection
     */

    if (data.getType() == WorldType.NORMAL) {
      content.set(1, 2, ClickableItem.of(new ItemBuilder(Material.GRASS).enchantment(Enchantment.ARROW_DAMAGE).setItemFlag(ItemFlag.HIDE_ENCHANTS)
          .name(ChatColor.GRAY + WorldType.NORMAL.getName()).build(), e -> {
        data.setType(WorldType.NORMAL);
        reopen(player, content);
      }));
    } else {
      content.set(1, 2, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + WorldType.NORMAL.getName()).build(), e -> {
        data.setType(WorldType.NORMAL);
        reopen(player, content);
      }));
    }

    if (data.getType() == WorldType.FLAT) {
      content.set(1, 4, ClickableItem.of(new ItemBuilder(Material.BEDROCK).enchantment(Enchantment.ARROW_DAMAGE).setItemFlag(ItemFlag.HIDE_ENCHANTS)
          .name(ChatColor.GRAY + WorldType.FLAT.getName()).build(), e -> {
        data.setType(WorldType.FLAT);
        reopen(player, content);
      }));
    } else {
      content.set(1, 4, ClickableItem.of(new ItemBuilder(Material.BEDROCK).name(ChatColor.GRAY + WorldType.FLAT.getName()).build(), e -> {
        data.setType(WorldType.FLAT);
        reopen(player, content);
      }));
    }

    if (data.getType() == WorldType.AMPLIFIED) {
      content.set(1, 6, ClickableItem.of(new ItemBuilder(Material.FEATHER).enchantment(Enchantment.ARROW_DAMAGE).setItemFlag(ItemFlag.HIDE_ENCHANTS)
          .name(ChatColor.GRAY + WorldType.AMPLIFIED.getName()).build(), e -> {
        data.setType(WorldType.AMPLIFIED);
        reopen(player, content);
      }));
    } else {
      content.set(1, 6, ClickableItem.of(new ItemBuilder(Material.FEATHER).name(ChatColor.GRAY + WorldType.AMPLIFIED.getName()).build(), e -> {
        data.setType(WorldType.AMPLIFIED);
        reopen(player, content);
      }));
    }

    if (data.getType() == WorldType.LARGE_BIOMES) {
      content.set(1, 7, ClickableItem.of(new ItemBuilder(Material.MYCELIUM).enchantment(Enchantment.ARROW_DAMAGE).setItemFlag(ItemFlag.HIDE_ENCHANTS)
          .name(ChatColor.GRAY + WorldType.LARGE_BIOMES.getName()).build(), e -> {
        data.setType(WorldType.LARGE_BIOMES);
        reopen(player, content);
      }));
    } else {
      content.set(1, 7, ClickableItem.of(new ItemBuilder(Material.MYCELIUM).name(ChatColor.GRAY + WorldType.LARGE_BIOMES.getName()).build(), e -> {
        data.setType(WorldType.LARGE_BIOMES);
        reopen(player, content);
      }));
    }

    content.set(3, 3, ClickableItem.of(new ItemBuilder(Material.NAME_TAG).name(Language.GUI_CREATOR_ENTER_GENERATOR_BUTTON.toString())
        .lore(Language.GUI_CREATOR_ENTER_GENERATOR_BUTTON_DESC.toString().replace("%generator%", data.getGenerator()))
        .build(), e ->
        new ChatInput(player, Language.GUI_CREATOR_ENTER_GENERATOR.toChatString(), false, input -> {

          ChunkGenerator generator = WorldCreator.getGeneratorForName(worldname, data.getGenerator(), player);

          if (generator == null) {
            Bukkit.getScheduler().runTaskLater(WorldManagerPlugin.getInstance(), () -> player.sendMessage(Language.GUI_CREATOR_ENTER_GENERATOR_ERROR.toChatString()), 1L);
          } else {
            data.setGenerator(input);
          }
          reopen(player, content);
        })));

    content.set(3, 5, ClickableItem.of(new ItemBuilder(Material.BEACON).name(Language.GUI_CREATOR_RANDOMIZE_SEED.toString())
        .lore(Language.GUI_CREATOR_RANDOMIZE_SEED_DESCRIPTION.toString().replace("%seed%", String.valueOf(data.getSeed())))
        .build(), e -> {
      data.setSeed(ThreadLocalRandom.current().nextLong());
      reopen(player, content);
    }));

    content.set(5, 4, ClickableItem.of(new ItemBuilder(Material.EMERALD).name(Language.GUI_CREATOR_RANDOMIZE_CREATEWORLD.toString().replace("%world%", worldname))
        .lore(Language.GUI_CREATOR_RANDOMIZE_CREATEWORLD_DESC.getDescriptionArray().stream()
            .map(line -> line.replace("%world%", worldname)
                .replace("%environment%", data.getEnvironment().toString())
                .replace("%type%", data.getType().toString())
                .replace("%generator%", data.getGenerator())
                .replace("%seed%", String.valueOf(data.getSeed())))
            .collect(Collectors.toList())
        )
        .build(), e -> {
      WorldConfiguration config = new WorldConfiguration(worldname, data.getEnvironment(), data.getType(), data.getGenerator(), ThreadLocalRandom.current().nextLong());
      player.closeInventory();
      WorldManagerPlugin.getInstance().getWorldManager().createWorld(config);
    }));
  }
}
