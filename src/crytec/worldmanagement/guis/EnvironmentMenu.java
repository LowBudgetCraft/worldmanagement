package crytec.worldmanagement.guis;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import crytec.worldmanagement.Language;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;

public class EnvironmentMenu implements InventoryProvider {

	@Override
	public void init(Player player, InventoryContents content) {		
		String name = content.property("worldname");
		
		content.set(0, 3, ClickableItem.of(new ItemBuilder(Material.GRASS).name(Language.ENVIRONMENT_NORMAL.toString()).build(), e -> {
			Menus.TYPE_MENU.open(player, new String[] { "worldname", "env"  }, new Object[] { name, Environment.NORMAL });
		}));
		
		content.set(0, 4, ClickableItem.of(new ItemBuilder(Material.NETHER_BRICK).name(Language.ENVIRONMENT_NETHER.toString()).build(), e -> {
			Menus.TYPE_MENU.open(player, new String[] { "worldname", "env"  }, new Object[] { name, Environment.NETHER });
		}));
		
		content.set(0, 5, ClickableItem.of(new ItemBuilder(Material.END_CRYSTAL).name(Language.ENVIRONMENT_END.toString()).build(), e -> {
			Menus.TYPE_MENU.open(player, new String[] { "worldname", "env"  }, new Object[] { name, Environment.THE_END });
		}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {}
}