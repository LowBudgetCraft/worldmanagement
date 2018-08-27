package crytec.worldmanagement.guis;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import crytec.worldmanagement.WorldManagerPlugin;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;
import net.crytec.api.util.F;
import net.crytec.api.util.UtilPlayer;

public class WorldSettingsGUI implements InventoryProvider {

	@Override
	public void init(Player player, InventoryContents content) {
		
		String worldname = content.property("world");
		World world = Bukkit.getWorld(worldname);
		
		boolean mainWorld = false;
		
		if (Bukkit.getWorlds().get(0) == world) {
			mainWorld = true;
		}
		
		// Back Button
		content.set(SlotPos.of(3, 0), ClickableItem.of(new ItemBuilder(Material.ARROW).name("§7Zurück").build(), e -> {
			content.inventory().getParent().get().open(player);
		}));
		
		// Teleport Button
		content.set(SlotPos.of(3, 4), ClickableItem.of(new ItemBuilder(Material.ENDER_PEARL).name("§2Teleport").build(), e -> {
					player.closeInventory();
					UtilPlayer.teleport(player, world.getSpawnLocation());
					player.sendMessage(F.main("WorldManagement", "Du wurdest an den Spawn der Welt " + F.name(world.getName()) + " teleportiert."));
					return;
		}));
		
		
		content.set(SlotPos.of(0, 0), ClickableItem.of(new ItemBuilder(Material.GREEN_BED)
					.name("§2Spawnpunkt setzen")
					.lore("§fSetzt den Spawnpunkt an deiner aktuellen Position.")
					.build(), e -> {
						if (player.getWorld() != world) {
							player.sendMessage(F.error("Du befindest dich nicht in der richtigen Welt!"));
							player.closeInventory();
							return;
						}
						world.setSpawnLocation(player.getLocation());
						player.sendMessage(F.main("WorldManagement", "Der Spawnpunkt der Welt " + F.name(world.getName()) + " wurde an deiner aktuellen Position gesetzt."));
					}));
		
		if (!mainWorld) {
		content.set(SlotPos.of(0, 1), ClickableItem.of(new ItemBuilder(Material.SPAWNER)
					.name("§2Monster Spawn")
					.lore("§fAktiviert: " + F.tf(world.getAllowMonsters()))
					.build(), e -> {
						world.setSpawnFlags(!world.getAllowMonsters(), world.getAllowAnimals());
						this.reOpenMenu(player, world);
					}));
		}
		
		if (!mainWorld) {
		content.set(SlotPos.of(0, 2), ClickableItem.of(new ItemBuilder(Material.WHITE_WOOL)
					.name("§2Tier Spawn")
					.lore("§fAktiviert: " + F.tf(world.getAllowAnimals()))
					.build(), e -> {
						world.setSpawnFlags(world.getAllowMonsters(), !world.getAllowAnimals());
						this.reOpenMenu(player, world);
					}));
		}
		
		if (!mainWorld) {
		content.set(SlotPos.of(0, 3), ClickableItem.of(new ItemBuilder(Material.DRAGON_EGG)
					.name("§2Schwierigkeit")
					.lore("§fVerändert die Schwwierigkeit der Welt.")
					.lore("§fAktuell:" + world.getDifficulty().name())
					.build(), e -> {
						world.setDifficulty(getNextDifficulty(world.getDifficulty()));
						this.reOpenMenu(player, world);
					}));
		}
		
		if (!mainWorld) {
		content.set(SlotPos.of(0, 4), ClickableItem.of(new ItemBuilder(Material.DIAMOND_SWORD)
					.name("§2PvP")
					.setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.lore("§fAktiviert/Deaktiviert das PvP")
					.lore("§fin dieser Welt.")
					.lore("§fAktiviert: " + F.tf(world.getPVP()))
					.build(), e -> {
						world.setPVP(!world.getPVP());
						this.reOpenMenu(player, world);
					}));
		}
		
		if (!mainWorld) {
		content.set(SlotPos.of(0, 5), ClickableItem.of(new ItemBuilder(Material.BEACON)
					.name("§2Spawn geladen lassen")
					.lore("§fLässt die Chunks um den Spawn")
					.lore("§fpermanent geladen.")
					.lore("§fTeleports an den Spawn dieser Welt")
					.lore("§fsind damit schneller.")
					.lore("§fAktiviert: " + F.tf(world.getKeepSpawnInMemory()))
					.build(), e -> {
						world.setKeepSpawnInMemory(!world.getKeepSpawnInMemory());
						this.reOpenMenu(player, world);
					}));
		}
		
		content.set(SlotPos.of(0, 6), ClickableItem.of(new ItemBuilder(Material.FLINT_AND_STEEL)
					.name("§2Feuer")
					.lore("§fAktiviert/Deaktiviert die")
					.lore("§fAusbreitung von Feuer.")
					.lore("§fAktiviert: " + F.tf(world.getGameRuleValue(GameRule.DO_FIRE_TICK)))
					.build(), e -> {
						world.setGameRule(GameRule.DO_FIRE_TICK, !world.getGameRuleValue(GameRule.DO_FIRE_TICK));
						this.reOpenMenu(player, world);
					}));
		
		content.set(SlotPos.of(0, 7), ClickableItem.of(new ItemBuilder(Material.CLOCK)
					.name("§2Zeit einfrieren")
					.lore("§fAktiviert/Deaktiviert das")
					.lore("§ffortschreiten der Zeit.")
					.lore("§fZeit eingefroren: " + F.tf(!world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)))
					.build(), e -> {
						world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, !world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE));
						this.reOpenMenu(player, world);
					}));
		
		content.set(SlotPos.of(0, 8), ClickableItem.of(new ItemBuilder(Material.WATER_BUCKET)
					.name("§2Wetter")
					.lore("§fAktiviert/Deaktiviert das")
					.lore("§fWetter in der Welt")
					.lore("§fAktiviert: " + F.tf(world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE)))
					.build(), e -> {
						world.setGameRule(GameRule.DO_WEATHER_CYCLE, !world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE));
						this.reOpenMenu(player, world);
					}));
		
		
		if (!WorldManagerPlugin.getInstance().getWorldManager().isMainWorld(world)) {
			content.set(SlotPos.of(1, 8), ClickableItem.of(new ItemBuilder(Material.REDSTONE_BLOCK)
						.name("§c§lWelt deaktivieren")
						.lore("§fDeaktivert die angegebene Welt")
						.lore("§fDiese wird bei einem Server neustart")
						.lore("§fnicht wieder automatisch geladen.")
						.build(), e -> {
				player.closeInventory();
				long start = System.currentTimeMillis();
				WorldManagerPlugin.getInstance().getWorldManager().unloadWorld(world, done -> {
					long stop = System.currentTimeMillis();
					player.sendMessage(F.main("WorldManagement", "Die Welt wurde deaktiviert. (" + (stop -start) + " ms)"));
				});
				
				
			}));
		}
		
		if (!WorldManagerPlugin.getInstance().getWorldManager().isMainWorld(world)) {
			content.set(SlotPos.of(2, 8), ClickableItem.of(new ItemBuilder(Material.TNT)
						.name("§c§lWelt löschen")
						.lore("§4Löscht die Welt")
						.lore("§4§lPERMANENT §cvom Server.")
						.lore("§cDieser Aktion lässt sich §4§lNICHT")
						.lore("§crückgängig machen.")
						.build(), e -> {
							Menus.DELETE_CONFIRM.open(player, new String[] { "world" }, new Object[] { world });
						}));
		}

		
		
		
		
	}

	@Override
	public void update(Player arg0, InventoryContents arg1) {		
	}
	
	private void reOpenMenu(Player player, World world) {
		Menus.WORLD_SETTINGS.open(player, new String[] { "world" }, new Object[] { world.getName() });
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

}
