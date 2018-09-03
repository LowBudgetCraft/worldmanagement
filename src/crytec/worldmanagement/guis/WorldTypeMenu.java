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
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;

public class WorldTypeMenu implements InventoryProvider {

	@Override
	public void init(Player player, InventoryContents content) {
		
		WorldManager manager = WorldManagerPlugin.getInstance().getWorldManager();
		Environment env = content.property("env");
		String worldname = content.property("worldname");
		
		content.set(0, 1, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + WorldType.NORMAL.getName()).build(), e -> {
			player.closeInventory();
			player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(worldname, env, WorldType.NORMAL, true);
			});
			
		}));
		content.set(0, 3, ClickableItem.of(new ItemBuilder(Material.BEDROCK).name(ChatColor.GRAY + WorldType.FLAT.getName()).build(), e -> {
			player.closeInventory();
			player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(worldname, env, WorldType.FLAT, true);
			});
			
		}));
		content.set(0, 5, ClickableItem.of(new ItemBuilder(Material.MYCELIUM).name(ChatColor.GRAY + WorldType.LARGE_BIOMES.getName()).build(), e -> {
			player.closeInventory();
			player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(worldname, env, WorldType.LARGE_BIOMES, true);
			});
			
		}));
		content.set(0, 7, ClickableItem.of(new ItemBuilder(Material.PHANTOM_MEMBRANE).name(ChatColor.GRAY + WorldType.AMPLIFIED.getName()).build(), e -> { 
			player.closeInventory();
			player.sendMessage(Language.GUI_WORLDTYPE_GENERATEINFO.toChatString());
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.createWorld(worldname, env, WorldType.AMPLIFIED, true);
			});
		}));				
	}

	@Override
	public void update(Player player, InventoryContents contents) {}
}