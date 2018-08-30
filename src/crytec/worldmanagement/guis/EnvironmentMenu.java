package crytec.worldmanagement.guis;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import crytec.worldmanagement.data.CEnvironment;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;

public class EnvironmentMenu implements InventoryProvider {

	@Override
	public void init(Player player, InventoryContents content) {		
		String name = content.property("worldname");
		
		content.set(0, 0, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + "Normal").build(), e -> {
			Menus.TYPE_MENU.open(player, new String[] { "worldname", "env"  }, new Object[] { name, CEnvironment.NORMAL });
		}));
		
		
		
		content.set(0, 1, ClickableItem.of(new ItemBuilder(Material.NETHER_BRICK).name(ChatColor.GRAY + "Nether").build(), e -> {
			Menus.TYPE_MENU.open(player, new String[] { "worldname", "env"  }, new Object[] { name, CEnvironment.NETHER });
		}));
		
		
		
		content.set(0, 2, ClickableItem.of(new ItemBuilder(Material.END_CRYSTAL).name(ChatColor.GRAY + "End").build(), e -> {
			Menus.TYPE_MENU.open(player, new String[] { "worldname", "env"  }, new Object[] { name, CEnvironment.THE_END });
		}));
		
		content.set(0, 8, ClickableItem.empty(new ItemBuilder(Material.SIGN).name(ChatColor.YELLOW + "Infos").lore("§eWeltname: §7" + content.property("worldname")).build()));
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {}
}