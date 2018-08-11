package crytec.worldmanagement.guis;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import crytec.worldmanagement.Worldmanagement;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.util.F;

public class EnvironmentMenu implements InventoryProvider {

	@Override
	public void init(Player player, InventoryContents content) {		
		
		
		content.set(0, 0, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + "Normal").build(), e -> {
			String name = content.property("worldname");
			player.closeInventory();
			player.sendMessage(F.main("Admin", "Eine neue Welt wird generiert..."));
			Worldmanagement.getInstance().createNewWorld(name, Environment.NORMAL, WorldType.NORMAL, content.property("generator"));
			
		}));
		
		
		
		content.set(0, 1, ClickableItem.of(new ItemBuilder(Material.NETHER_BRICK).name(ChatColor.GRAY + "Nether").build(), e -> {
			String name = content.property("worldname");
			player.closeInventory();
			player.sendMessage(F.main("Admin", "Eine neue Welt wird generiert..."));
			Worldmanagement.getInstance().createNewWorld(name, Environment.NETHER, WorldType.NORMAL, content.property("generator"));
			
		}));
		
		
		
		content.set(0, 2, ClickableItem.of(new ItemBuilder(Material.END_CRYSTAL).name(ChatColor.GRAY + "End").build(), e -> {
			String name = content.property("worldname");
			player.closeInventory();
			player.sendMessage(F.main("Admin", "Eine neue Welt wird generiert..."));
			Worldmanagement.getInstance().createNewWorld(name, Environment.THE_END, WorldType.NORMAL, content.property("generator"));
		}));

		
		
		
		
		
		content.set(0, 8, ClickableItem.empty(new ItemBuilder(Material.SIGN).name(ChatColor.YELLOW + "Infos").lore("§eWeltname: §7" + content.property("worldname"))
				.lore("§eGenerator: §7" + content.property("generator"))
				.build()));
		
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {}
}