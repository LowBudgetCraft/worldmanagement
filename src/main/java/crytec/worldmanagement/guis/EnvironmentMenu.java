package crytec.worldmanagement.guis;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import crytec.worldmanagement.Language;
import net.crytec.phoenix.api.inventory.ClickableItem;
import net.crytec.phoenix.api.inventory.SmartInventory;
import net.crytec.phoenix.api.inventory.content.InventoryContents;
import net.crytec.phoenix.api.inventory.content.InventoryProvider;
import net.crytec.phoenix.api.item.ItemBuilder;

public class EnvironmentMenu implements InventoryProvider {
	
	public EnvironmentMenu(String worldname) {
		this.worldname = worldname;
	}
	
	private final String worldname;

	@Override
	public void init(Player player, InventoryContents content) {		
		
		content.set(0, 3, ClickableItem.of(new ItemBuilder(Material.GRASS).name(Language.ENVIRONMENT_NORMAL.toString()).build(), e -> {
			
			SmartInventory.builder()
			.provider(new WorldTypeMenu(Environment.NORMAL, this.worldname))
			.size(1, 9)
			.title(Language.GUI_TITLE_WORLDTYPE.toString())
			.build().open(player);
			
		}));
		
		content.set(0, 4, ClickableItem.of(new ItemBuilder(Material.NETHER_BRICK).name(Language.ENVIRONMENT_NETHER.toString()).build(), e -> {
			SmartInventory.builder()
			.provider(new WorldTypeMenu(Environment.NETHER, this.worldname))
			.size(1, 9)
			.title(Language.GUI_TITLE_WORLDTYPE.toString())
			.build().open(player);
		}));
		
		content.set(0, 5, ClickableItem.of(new ItemBuilder(Material.END_CRYSTAL).name(Language.ENVIRONMENT_END.toString()).build(), e -> {
			SmartInventory.builder()
			.provider(new WorldTypeMenu(Environment.THE_END, this.worldname))
			.size(1, 9)
			.title(Language.GUI_TITLE_WORLDTYPE.toString())
			.build().open(player);
		}));
	}
}