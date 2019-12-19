package crytec.worldmanagement.guis;

import crytec.worldmanagement.Language;
import crytec.worldmanagement.WorldManager;
import crytec.worldmanagement.WorldManagerPlugin;
import crytec.worldmanagement.data.WorldConfiguration;
import crytec.worldmanagement.utils.ItemBuilder;
import java.util.stream.Collectors;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class WorldSettingsGUI implements InventoryProvider {


  public WorldSettingsGUI(WorldConfiguration config) {
    this.config = config;
    manager = WorldManagerPlugin.getInstance().getWorldManager();
    world = Bukkit.getWorld(config.getWorldName());

  }

  private final WorldManager manager;
  private final WorldConfiguration config;
  private final World world;


  @Override
  public void init(Player player, InventoryContent content) {
    // Back Button
    content.set(SlotPos.of(4, 0), ClickableItem.of(new ItemBuilder(Material.ARROW).name(Language.GUI_GENERAL_BACK.toString()).build(), e -> {
      content.getHost().getParent().get().open(player);
    }));

    if (world != null) {
      // Teleport Button
      content.set(SlotPos.of(4, 4), ClickableItem.of(new ItemBuilder(Material.ENDER_PEARL)
          .name(Language.GUI_SETTINGS_TELEPORT.toString())
          .lore(Language.GUI_SETTINGS_TELEPORT_DESC.getDescriptionArray())
          .build(), e -> {
        player.closeInventory();
        player.teleport(world.getSpawnLocation());
        player.sendMessage(Language.GUI_SETTINGS_TELEPORTFEEDBACK.toChatString().replace("%world%", world.getName()));
      }));

      content.set(SlotPos.of(3, 4), ClickableItem.of(new ItemBuilder(Material.GREEN_BED)
          .name(Language.GUI_SETTINGS_SPAWNPOINT.toString())
          .lore(Language.GUI_SETTINGS_SPAWNPOINT_DESC.getDescriptionArray())
          .build(), e -> {
        if (player.getWorld() != world) {
          player.sendMessage(Language.ERROR_WRONG_WORLD.toChatString());
          player.closeInventory();
          return;
        }
        world.setSpawnLocation(player.getLocation());
        player.sendMessage(Language.GUI_SETTINGS_SPAWNPOINT_FEEDBACK.toChatString());
      }));

      content.set(SlotPos.of(0, 0), ClickableItem.of(new ItemBuilder(Material.SPAWNER)
          .name(Language.GUI_SETTINGS_MOBSPAWN.toString())
          .lore(Language.GUI_SETTINGS_MOBSPAWN_DESC.getDescriptionArray()
              .stream().map(s -> s.replace("%status%", WorldSettingsGUI.tf(world.getAllowMonsters()))).collect(Collectors.toList()))
          .build(), e -> {
        config.setMonsterspawn(!world.getAllowMonsters());
        reopen(player, content);
      }));

      content.set(SlotPos.of(1, 0), ClickableItem.of(new ItemBuilder(Material.WHITE_WOOL)
          .name(Language.GUI_SETTINGS_ANIMALSPAWN.toString())
          .lore(Language.GUI_SETTINGS_ANIMALSPAWN_DESC.getDescriptionArray()
              .stream().map(s -> s.replace("%status%", WorldSettingsGUI.tf(world.getAllowAnimals()))).collect(Collectors.toList()))
          .build(), e -> {
        config.setAnimalspawn(!world.getAllowAnimals());
        reopen(player, content);
      }));

      content.set(SlotPos.of(0, 2), ClickableItem.of(new ItemBuilder(Material.DRAGON_EGG)
          .name(Language.GUI_SETTINGS_DIFFICULTY.toString())
          .lore(Language.GUI_SETTINGS_DIFFICULTY_DESC.getDescriptionArray()
              .stream().map(s -> s.replace("%status%", world.getDifficulty().toString())).collect(Collectors.toList()))
          .build(), e -> {
        config.setDifficulty(WorldSettingsGUI.getNextDifficulty(world.getDifficulty()));
        reopen(player, content);
      }));

      content.set(SlotPos.of(1, 2), ClickableItem.of(new ItemBuilder(Material.DIAMOND_SWORD)
          .name(Language.GUI_SETTINGS_PVP.toString())
          .lore(Language.GUI_SETTINGS_PVP_DESC.getDescriptionArray()
              .stream().map(s -> s.replace("%status%", WorldSettingsGUI.tf(world.getPVP()))).collect(Collectors.toList()))
          .setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
          .build(), e -> {
        config.setPvp(!world.getPVP());
        reopen(player, content);
      }));

      content.set(SlotPos.of(2, 0), ClickableItem.of(new ItemBuilder(Material.BEACON)
          .name(Language.GUI_SETTINGS_SPAWNCHUNKS.toString())
          .lore(Language.GUI_SETTINGS_SPAWNCHUNKS_DESC.getDescriptionArray()
              .stream().map(s -> s.replace("%status%", WorldSettingsGUI.tf(world.getKeepSpawnInMemory()))).collect(Collectors.toList()))
          .build(), e -> {
        config.setKeepSpawnLoaded(!world.getKeepSpawnInMemory());
        reopen(player, content);
      }));

      content.set(SlotPos.of(0, 6), ClickableItem.of(new ItemBuilder(Material.FLINT_AND_STEEL)
          .name(Language.GUI_SETTINGS_FIRESPREAD.toString())
          .lore(Language.GUI_SETTINGS_FIRESPREAD_DESC.getDescriptionArray()
              .stream().map(s -> s.replace("%status%", WorldSettingsGUI.tf(world.getGameRuleValue(GameRule.DO_FIRE_TICK)))).collect(Collectors.toList()))
          .build(), e -> {
        world.setGameRule(GameRule.DO_FIRE_TICK, !world.getGameRuleValue(GameRule.DO_FIRE_TICK));
        reopen(player, content);
      }));

      content.set(SlotPos.of(0, 7), ClickableItem.of(new ItemBuilder(Material.CLOCK)
          .name(Language.GUI_SETTINGS_TIME.toString())
          .lore(Language.GUI_SETTINGS_TIME_DESC.getDescriptionArray()
              .stream().map(s -> s.replace("%status%", WorldSettingsGUI.tf(!world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)))).collect(Collectors.toList()))
          .build(), e -> {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, !world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE));
        reopen(player, content);
      }));

      content.set(SlotPos.of(0, 8), ClickableItem.of(new ItemBuilder(Material.WATER_BUCKET)
          .name(Language.GUI_SETTINGS_WEATHER.toString())
          .lore(Language.GUI_SETTINGS_WEATHER_DESC.getDescriptionArray()
              .stream().map(s -> s.replace("%status%", WorldSettingsGUI.tf(!world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE)))).collect(Collectors.toList()))
          .build(), e -> {
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, !world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE));
        reopen(player, content);
      }));

      content.set(SlotPos.of(4, 6), ClickableItem.empty(new ItemBuilder(Material.PAPER)
          .name(Language.GUI_SETTINGS_INFORMATION.toString())
          .lore(Language.GUI_SETTINGS_INFORMATION_DESCRIPTION.getDescriptionArray()
              .stream().map(line ->
                  line.replace("%worldtype%", world.getWorldType().toString())
                      .replace("%environment%", world.getEnvironment().toString())
                      .replace("%seed%", String.valueOf(world.getSeed()))
                      .replace("%entities%", String.valueOf(world.getEntities().size()))
                      .replace("%chunks%", "" + String.valueOf(world.getLoadedChunks().length))
              ).collect(Collectors.toList()))
          .build()));

      content.set(SlotPos.of(0, 4), ClickableItem.of(new ItemBuilder(Material.ARMOR_STAND)
          .name(Language.GUI_SETTINGS_GAMEMODE.toString())
          .lore(Language.GUI_SETTINGS_GAMEMODE_DESCRIPTION.getDescriptionArray()
              .stream().map(s -> s.replace("%status%", config.getForcedGameMode().toString())).collect(Collectors.toList()))
          .build(), e -> {
        config.setGameMode(WorldSettingsGUI.getNextGameMode(config.getForcedGameMode()));
        reopen(player, content);
      }));

      // Delete world permanently
      if (!WorldManager.isMainWorld(world)) {
        content.set(SlotPos.of(4, 8), ClickableItem.of(new ItemBuilder(Material.TNT)
            .name(Language.GUI_SETTINGS_DELETION.toString())
            .lore(Language.GUI_SETTINGS_DELETION_DESCRIPTION.getDescriptionArray())
            .build(), e -> {

          SmartInventory.builder()
              .provider(new WorldDeleteConfirm(world))
              .size(3, 9)
              .title(Language.GUI_TITLE_DELETEION.toString())
              .build().open(player);

        }));
      }
    }

    if (!config.isEnabled()) {
      content.set(SlotPos.of(4, 1), ClickableItem.of(new ItemBuilder(Material.EMERALD_BLOCK)
          .name(Language.GUI_SETTINGS_ACTIVATEWORLD.toString())
          .lore(Language.GUI_SETTINGS_ACTIVATEWORLD_DESC.getDescriptionArray())
          .build(), e -> {
        player.closeInventory();
        config.setEnabled(true);
        manager.createWorld(config);
        player.sendMessage(Language.GUI_ACTIVATING_WORLD.toChatString().replace("%world%", config.getWorldName()));
      }));
    }

    if (config.isEnabled()) {
      content.set(SlotPos.of(4, 2), ClickableItem.of(new ItemBuilder(Material.REDSTONE_BLOCK)
          .name(Language.GUI_SETTINGS_DISABLEWORLD.toString())
          .lore(Language.GUI_SETTINGS_DISABLEWORLD_DESC.getDescriptionArray())
          .build(), e -> {
        player.closeInventory();
        long start = System.currentTimeMillis();
        WorldManagerPlugin.getInstance().getWorldManager().unloadWorld(world, done -> {
          long stop = System.currentTimeMillis();
          player.sendMessage(Language.GUI_SETTINGS_DISABLE_FEEDBACK.toChatString().replace("%time%", (stop - start) + "ms"));
          config.setEnabled(false);
        });
      }));
    }

  }

  private static String tf(boolean mode) {
    return mode ? "True" : "False";
  }

  private static Difficulty getNextDifficulty(Difficulty current) {
    switch (current) {
      case PEACEFUL:
        return Difficulty.EASY;
      case EASY:
        return Difficulty.NORMAL;
      case NORMAL:
        return Difficulty.HARD;
      case HARD:
        return Difficulty.PEACEFUL;
      default:
        return Difficulty.EASY;
    }
  }

  private static GameMode getNextGameMode(GameMode current) {
    switch (current) {
      case SURVIVAL:
        return GameMode.CREATIVE;
      case CREATIVE:
        return GameMode.ADVENTURE;
      case ADVENTURE:
        return GameMode.SPECTATOR;
      case SPECTATOR:
        return GameMode.SURVIVAL;
      default:
        return GameMode.SURVIVAL;
    }
  }
}