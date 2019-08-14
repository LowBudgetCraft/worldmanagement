package crytec.worldmanagement.guis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import crytec.worldmanagement.Language;
import crytec.worldmanagement.WorldManager;
import crytec.worldmanagement.WorldManagerPlugin;
import net.crytec.phoenix.api.inventory.ClickableItem;
import net.crytec.phoenix.api.inventory.content.InventoryContents;
import net.crytec.phoenix.api.inventory.content.InventoryProvider;
import net.crytec.phoenix.api.item.ItemBuilder;

public class WorldTypeMenu implements InventoryProvider {
	
	public WorldTypeMenu(Environment environment, String worldname) {
		this.manager = WorldManagerPlugin.getInstance().getWorldManager();
		this.environment = environment;
		this.worldname = worldname;
	}
	
	private final WorldManager manager;
	private final Environment environment;
	private final String worldname;

	@Override
	public void init(Player player, InventoryContents content) {
		
		content.set(0, 1, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + WorldType.NORMAL.getName()).build(), e -> {
			player.closeInventory();
			player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(this.worldname, this.environment, WorldType.NORMAL);
			});
			
		}));
		content.set(0, 3, ClickableItem.of(new ItemBuilder(Material.BEDROCK).name(ChatColor.GRAY + WorldType.FLAT.getName()).build(), e -> {
			player.closeInventory();
			player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(this.worldname, this.environment, WorldType.FLAT);
			});
			
		}));
		content.set(0, 5, ClickableItem.of(new ItemBuilder(Material.MYCELIUM).name(ChatColor.GRAY + WorldType.LARGE_BIOMES.getName()).build(), e -> {
			player.closeInventory();
			player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(this.worldname, this.environment, WorldType.LARGE_BIOMES);
			});
			
		}));
		content.set(0, 7, ClickableItem.of(new ItemBuilder(Material.PHANTOM_MEMBRANE).name(ChatColor.GRAY + WorldType.AMPLIFIED.getName()).build(), e -> { 
			player.closeInventory();
			player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(this.worldname, this.environment, WorldType.AMPLIFIED);
			});
		}));				
	}
}