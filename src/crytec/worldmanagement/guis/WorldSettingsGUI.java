package crytec.worldmanagement.guis;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import crytec.worldmanagement.Worldmanagement;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;
import net.crytec.api.util.F;

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
		
		
		content.set(SlotPos.of(0, 0), ClickableItem.of(new ItemBuilder(Material.GREEN_BED)
					.name("Spawnpunkt setzen")
					.lore("§7Setzt den Spawnpunkt an deiner aktuellen Position.")
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
					.name("Monster Spawn")
					.lore("Aktiviert: " + F.tf(world.getAllowMonsters()))
					.build(), e -> {
						world.setSpawnFlags(!world.getAllowMonsters(), world.getAllowAnimals());
						this.reOpenMenu(player, world);
					}));
		}
		
		if (!mainWorld) {
		content.set(SlotPos.of(0, 2), ClickableItem.of(new ItemBuilder(Material.WHITE_WOOL)
					.name("Tier Spawn")
					.lore("Aktiviert: " + F.tf(world.getAllowAnimals()))
					.build(), e -> {
						world.setSpawnFlags(world.getAllowMonsters(), !world.getAllowAnimals());
						this.reOpenMenu(player, world);
					}));
		}
		
		if (!mainWorld) {
		content.set(SlotPos.of(0, 3), ClickableItem.of(new ItemBuilder(Material.DRAGON_EGG)
					.name("Schwierigkeit")
					.lore(world.getDifficulty().name())
					.build(), e -> {
						world.setDifficulty(getNextDifficulty(world.getDifficulty()));
						this.reOpenMenu(player, world);
					}));
		}
		
		if (!mainWorld) {
		content.set(SlotPos.of(0, 4), ClickableItem.of(new ItemBuilder(Material.DIAMOND_SWORD)
					.name("PvP")
					.lore("Aktiviert: " + F.tf(world.getPVP()))
					.build(), e -> {
						world.setPVP(!world.getPVP());
						this.reOpenMenu(player, world);
					}));
		}
		
		if (!mainWorld) {
		content.set(SlotPos.of(0, 5), ClickableItem.of(new ItemBuilder(Material.DIAMOND_SWORD)
					.name("Keep Spawn loaded")
					.lore("Aktiviert: " + F.tf(world.getKeepSpawnInMemory()))
					.build(), e -> {
						world.setKeepSpawnInMemory(!world.getKeepSpawnInMemory());
						this.reOpenMenu(player, world);
					}));
		}
		
		content.set(SlotPos.of(0, 6), ClickableItem.of(new ItemBuilder(Material.DIAMOND_SWORD)
					.name("FireTick")
					.lore("Aktiviert: " + F.tf(Boolean.valueOf(world.getGameRuleValue("doFireTick"))))
					.build(), e -> {
						world.setGameRuleValue("doFireTick", String.valueOf(!Boolean.valueOf(world.getGameRuleValue("doFireTick"))));
						world.setPVP(!world.getPVP());
						this.reOpenMenu(player, world);
					}));
		
		
		content.set(SlotPos.of(1, 8), ClickableItem.of(new ItemBuilder(Material.REDSTONE_BLOCK)
					.name("§c§lWelt deaktivieren")
					.build(), e -> {
						player.closeInventory();
						Worldmanagement.getInstance().getWorldConfig(world).setEnabled(false);
						player.sendMessage(F.main("WorldManagement", "Die Welt wurde deaktiviert."));
					}));
		
		
		content.set(SlotPos.of(2, 8), ClickableItem.of(new ItemBuilder(Material.TNT)
					.name("§c§lWelt löschen")
					.build(), e -> {
						player.closeInventory();
						Worldmanagement.getInstance().deleteWorld(world, status -> {
							player.sendMessage(F.main("WorldManagement", "Die Welt wurde erfolgreich gelöscht."));
						});
					}));
		
		
		
		
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
