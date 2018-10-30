package crytec.worldmanagement.guis;

import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import crytec.worldmanagement.Language;
import crytec.worldmanagement.WorldManagerPlugin;
import crytec.worldmanagement.data.WorldConfiguration;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;
import net.crytec.api.util.F;

public class WorldSettingsGUI implements InventoryProvider {

	@Override
	public void init(Player player, InventoryContents content) {
		WorldConfiguration config = content.property("config");
		World world = Bukkit.getWorld(config.getWorldName());
		
		
		// Back Button
		content.set(SlotPos.of(4, 0), ClickableItem.of(new ItemBuilder(Material.ARROW).name(Language.GUI_GENERAL_BACK.toString()).build(), e -> {
			content.inventory().getParent().get().open(player);
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
					return;
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
								.stream().map(s -> s.replace("%status%", F.tf(world.getAllowMonsters()))).collect(Collectors.toList()))
					.build(), e -> {
						world.setSpawnFlags(!world.getAllowMonsters(), world.getAllowAnimals());
						this.reOpenMenu(player, config);
					}));
		
		content.set(SlotPos.of(1, 0), ClickableItem.of(new ItemBuilder(Material.WHITE_WOOL)
					.name(Language.GUI_SETTINGS_ANIMALSPAWN.toString())
					.lore(Language.GUI_SETTINGS_ANIMALSPAWN_DESC.getDescriptionArray()
								.stream().map(s -> s.replace("%status%", F.tf(world.getAllowAnimals()))).collect(Collectors.toList()))
					.build(), e -> {
						world.setSpawnFlags(world.getAllowMonsters(), !world.getAllowAnimals());
						this.reOpenMenu(player, config);
					}));
		
		content.set(SlotPos.of(0, 2), ClickableItem.of(new ItemBuilder(Material.DRAGON_EGG)
					.name(Language.GUI_SETTINGS_DIFFICULTY.toString())
					.lore(Language.GUI_SETTINGS_DIFFICULTY_DESC.getDescriptionArray()
								.stream().map(s -> s.replace("%status%", world.getDifficulty().toString())).collect(Collectors.toList()))
					.build(), e -> {
						world.setDifficulty(getNextDifficulty(world.getDifficulty()));
						this.reOpenMenu(player, config);
					}));
		
		content.set(SlotPos.of(1, 2), ClickableItem.of(new ItemBuilder(Material.DIAMOND_SWORD)
					.name(Language.GUI_SETTINGS_PVP.toString())
					.lore(Language.GUI_SETTINGS_PVP_DESC.getDescriptionArray()
								.stream().map(s -> s.replace("%status%", F.tf(world.getPVP()))).collect(Collectors.toList()))
					.setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.build(), e -> {
						world.setPVP(!world.getPVP());
						this.reOpenMenu(player, config);
					}));
		
		content.set(SlotPos.of(2, 0), ClickableItem.of(new ItemBuilder(Material.BEACON)
					.name(Language.GUI_SETTINGS_SPAWNCHUNKS.toString())
					.lore(Language.GUI_SETTINGS_SPAWNCHUNKS_DESC.getDescriptionArray()
								.stream().map(s -> s.replace("%status%", F.tf(world.getKeepSpawnInMemory()))).collect(Collectors.toList()))
					.build(), e -> {
						world.setKeepSpawnInMemory(!world.getKeepSpawnInMemory());
						this.reOpenMenu(player, config);
					}));
		

		
		content.set(SlotPos.of(0, 6), ClickableItem.of(new ItemBuilder(Material.FLINT_AND_STEEL)
					.name(Language.GUI_SETTINGS_FIRESPREAD.toString())
					.lore(Language.GUI_SETTINGS_FIRESPREAD_DESC.getDescriptionArray()
								.stream().map(s -> s.replace("%status%", F.tf(world.getGameRuleValue(GameRule.DO_FIRE_TICK)))).collect(Collectors.toList()))
					.build(), e -> {
						world.setGameRule(GameRule.DO_FIRE_TICK, !world.getGameRuleValue(GameRule.DO_FIRE_TICK));
						this.reOpenMenu(player, config);
					}));
		
		content.set(SlotPos.of(0, 7), ClickableItem.of(new ItemBuilder(Material.CLOCK)
					.name(Language.GUI_SETTINGS_TIME.toString())
					.lore(Language.GUI_SETTINGS_TIME_DESC.getDescriptionArray()
								.stream().map(s -> s.replace("%status%", F.tf(!world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)))).collect(Collectors.toList()))
					.build(), e -> {
						world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, !world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE));
						this.reOpenMenu(player, config);
					}));
		
		content.set(SlotPos.of(0, 8), ClickableItem.of(new ItemBuilder(Material.WATER_BUCKET)
					.name(Language.GUI_SETTINGS_WEATHER.toString())
					.lore(Language.GUI_SETTINGS_WEATHER_DESC.getDescriptionArray()
								.stream().map(s -> s.replace("%status%", F.tf(!world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE)))).collect(Collectors.toList()))
					.build(), e -> {
						world.setGameRule(GameRule.DO_WEATHER_CYCLE, !world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE));
						this.reOpenMenu(player, config);
					}));
		
		content.set(SlotPos.of(4, 6), ClickableItem.empty(new ItemBuilder(Material.SIGN)
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
		
		content.set(SlotPos.of(0, 0), ClickableItem.of(new ItemBuilder(Material.ARMOR_STAND)
					.name(Language.GUI_SETTINGS_GAMEMODE.toString())
					.lore(Language.GUI_SETTINGS_GAMEMODE.getDescriptionArray()
					.stream().map(s -> s.replace("%status%", config.getForcedGameMode().toString())).collect(Collectors.toList()))
					.build(), e -> {
						config.setGameMode(this.getNextGameMode(config.getForcedGameMode()));
						this.reOpenMenu(player, config);
					}));
		
		
		
		
		
		// Welt permanent löschen
		if (!WorldManagerPlugin.getInstance().getWorldManager().isMainWorld(world)) {
						content.set(SlotPos.of(4, 8), ClickableItem.of(new ItemBuilder(Material.TNT)
								.name(Language.GUI_SETTINGS_DELETION.toString())
								.lore(Language.GUI_SETTINGS_DELETION_DESCRIPTION.getDescriptionArray())
								.build(), e -> Menus.DELETE_CONFIRM.open(player, new String[] { "world" }, new Object[] { world })));
			}

		}
		
		
			if (!config.isEnabled()) {
			content.set(SlotPos.of(4, 1), ClickableItem.of(new ItemBuilder(Material.EMERALD_BLOCK)
						.name(Language.GUI_SETTINGS_ACTIVATEWORLD.toString())
						.lore(Language.GUI_SETTINGS_ACTIVATEWORLD_DESC.getDescriptionArray())
						.build(), e -> {
				player.closeInventory();
				config.setEnabled(true);
				WorldManagerPlugin.getInstance().getWorldManager().loadExistingWorld(config, true);
				player.sendMessage(F.main("WorldManagement", "Die Welt " + F.name(config.getWorldName()) + " wird aktiviert..."));
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
					player.sendMessage(Language.GUI_SETTINGS_DISABLE_FEEDBACK.toChatString().replace("%time%", String.valueOf((stop -start)) + "ms" ));
					config.setEnabled(false);
				});
			 }));
			}

	}

	@Override
	public void update(Player arg0, InventoryContents arg1) { }
	
	private void reOpenMenu(Player player, WorldConfiguration config) {
		Menus.WORLD_SETTINGS.open(player, new String[] { "config" }, new Object[] { config });
	}
	
	private Difficulty getNextDifficulty(Difficulty current) {
		switch (current) {
		case PEACEFUL: return Difficulty.EASY; 
		case EASY:  return Difficulty.NORMAL; 
		case NORMAL:  return Difficulty.HARD; 
		case HARD:  return Difficulty.PEACEFUL; 
		default: return Difficulty.EASY;
		}
	}
	
	private GameMode getNextGameMode(GameMode current) {
		switch (current) {
		case SURVIVAL: return GameMode.CREATIVE; 
		case CREATIVE:  return GameMode.ADVENTURE; 
		case ADVENTURE:  return GameMode.SPECTATOR; 
		case SPECTATOR:  return GameMode.SURVIVAL; 
		default: return GameMode.SURVIVAL;
		}
	}
}