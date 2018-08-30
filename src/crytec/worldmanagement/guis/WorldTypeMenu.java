package crytec.worldmanagement.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import crytec.worldmanagement.WorldManager;
import crytec.worldmanagement.WorldManagerPlugin;
import crytec.worldmanagement.data.CEnvironment;
import crytec.worldmanagement.data.CWorldType;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.util.F;

public class WorldTypeMenu implements InventoryProvider {

	@Override
	public void init(Player player, InventoryContents content) {
		
		WorldManager manager = WorldManagerPlugin.getInstance().getWorldManager();
		CEnvironment env = content.property("env");
		String worldname = content.property("worldname");
		
		content.set(0, 0, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + CWorldType.NORMAL.getName()).lore(CWorldType.NORMAL.getDescription()).build(), e -> {
			player.closeInventory();
			player.sendMessage(F.main("WorldManagement", "Eine neue Welt wird generiert..."));
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(worldname, env, WorldType.NORMAL, true);
			});
			
		}));
		content.set(0, 1, ClickableItem.of(new ItemBuilder(Material.BEDROCK).name(ChatColor.GRAY + CWorldType.FLAT.getName()).lore(CWorldType.FLAT.getDescription()).build(), e -> {
			player.closeInventory();
			player.sendMessage(F.main("WorldManagement", "Eine neue Welt wird generiert..."));
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(worldname, env, WorldType.FLAT, true);
			});
			
		}));
		content.set(0, 2, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + CWorldType.LARGE_BIOMES.getName()).lore(CWorldType.LARGE_BIOMES.getDescription()).build(), e -> {
			player.closeInventory();
			player.sendMessage(F.main("WorldManagement", "Eine neue Welt wird generiert..."));
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(worldname, env, WorldType.LARGE_BIOMES, true);
			});
			
		}));
		content.set(0, 3, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + CWorldType.AMPLIFIED.getName()).lore(CWorldType.AMPLIFIED.getDescription()).build(), e -> { 
			player.closeInventory();
			player.sendMessage(F.main("WorldManagement", "Eine neue Welt wird generiert..."));
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(worldname, env, WorldType.AMPLIFIED, true);
			});
		}));
		
		content.set(0, 8, ClickableItem.empty(new ItemBuilder(Material.SIGN).name(ChatColor.YELLOW + "Infos")
					.lore("§eWeltname: §7" + content.property("worldname"))
					.lore("§eUmgebung: §7" + env.getDisplayname())
					.build()));
				
	}

	@Override
	public void update(Player player, InventoryContents contents) {}
}