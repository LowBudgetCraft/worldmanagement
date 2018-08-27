package crytec.worldmanagement.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import crytec.worldmanagement.WorldManager;
import crytec.worldmanagement.WorldManagerPlugin;
import crytec.worldmanagement.data.CEnvironment;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.util.F;

public class EnvironmentMenu implements InventoryProvider {

	@Override
	public void init(Player player, InventoryContents content) {		
		
		WorldManager manager = WorldManagerPlugin.getInstance().getWorldManager();
		
		content.set(0, 0, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + "Normal").build(), e -> {
			String name = content.property("worldname");
			player.closeInventory();
			player.sendMessage(F.main("Admin", "Eine neue Welt wird generiert..."));
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(name, CEnvironment.NORMAL, WorldType.NORMAL);
			});
			
			
		}));
		
		
		
		content.set(0, 1, ClickableItem.of(new ItemBuilder(Material.NETHER_BRICK).name(ChatColor.GRAY + "Nether").build(), e -> {
			String name = content.property("worldname");
			player.closeInventory();
			player.sendMessage(F.main("Admin", "Eine neue Welt wird generiert..."));
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(name, CEnvironment.NETHER, WorldType.NORMAL);
			});
			
			
		}));
		
		
		
		content.set(0, 2, ClickableItem.of(new ItemBuilder(Material.END_CRYSTAL).name(ChatColor.GRAY + "End").build(), e -> {
			String name = content.property("worldname");
			player.closeInventory();
			player.sendMessage(F.main("Admin", "Eine neue Welt wird generiert..."));
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(name, CEnvironment.THE_END, WorldType.NORMAL);
			});
			
		}));
		
		content.set(0, 3, ClickableItem.of(new ItemBuilder(Material.GLASS).name(ChatColor.GRAY + "Void").build(), e -> {
			String name = content.property("worldname");
			player.closeInventory();
			player.sendMessage(F.main("Admin", "Eine neue Welt wird generiert..."));
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(name, CEnvironment.VOID, WorldType.NORMAL);
			});
			
		}));
		
		content.set(0, 8, ClickableItem.empty(new ItemBuilder(Material.SIGN).name(ChatColor.YELLOW + "Infos").lore("§eWeltname: §7" + content.property("worldname")).build()));
		
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {}
}