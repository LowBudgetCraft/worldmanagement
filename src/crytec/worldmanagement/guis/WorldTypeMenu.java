package crytec.worldmanagement.guis;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import crytec.worldmanagement.data.CWorldType;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;

public class WorldTypeMenu implements InventoryProvider {

	@Override
	public void init(Player player, InventoryContents content) {		
		content.set(0, 0, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + CWorldType.NORMAL.getName()).lore(CWorldType.NORMAL.getDescription()).build(), e -> { }));
		content.set(0, 1, ClickableItem.of(new ItemBuilder(Material.BEDROCK).name(ChatColor.GRAY + CWorldType.FLAT.getName()).lore(CWorldType.FLAT.getDescription()).build(), e -> { }));
		content.set(0, 2, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + CWorldType.LARGE_BIOMES.getName()).lore(CWorldType.LARGE_BIOMES.getDescription()).build(), e -> { }));
		content.set(0, 3, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + CWorldType.AMPLIFIED.getName()).lore(CWorldType.AMPLIFIED.getDescription()).build(), e -> { }));
		content.set(0, 4, ClickableItem.of(new ItemBuilder(Material.GRASS).name(ChatColor.GRAY + CWorldType.VOID.getName()).lore(CWorldType.VOID.getDescription()).build(), e -> { }));
		
		
		content.set(0, 8, ClickableItem.empty(new ItemBuilder(Material.SIGN).name(ChatColor.YELLOW + "Infos").lore("§eWeltname: §7" + content.property("worldname"))
				.lore("§eGenerator: §7" + content.property("generator"))
				.build()));
		
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {}
}