package crytec.worldmanagement.guis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import crytec.worldmanagement.Language;
import crytec.worldmanagement.WorldManagerPlugin;
import crytec.worldmanagement.data.WorldConfiguration;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.Pagination;
import net.crytec.api.smartInv.content.SlotIterator.Type;
import net.crytec.api.util.ChatStringInput;

public class WorldMainMenu implements InventoryProvider {
	
	public static <T> void replaceIf(List<T> list, Predicate<T> predicate, T replacement) {
	    for (int i = 0; i < list.size(); ++i)
	        if (predicate.test(list.get(i)))
	            list.set(i, replacement);
	}

	@Override
	public void init(Player player, InventoryContents content) {

		Pagination pagination = content.pagination();
		ArrayList<ClickableItem> items = new ArrayList<ClickableItem>();
		
		for (WorldConfiguration config : WorldManagerPlugin.getInstance().getWorldManager().getWorldConfigurations()) {
			
			ItemBuilder builder;
			
			if (config.isEnabled()) {
				builder = new ItemBuilder(Material.GREEN_WOOL);
				List<String> description = Language.GUI_MAIN_DESCRIPTION_ENABLED.getDescriptionArray();				
				description = description.stream().map(line -> 
				line.replace("%worldtype%", config.getWorldType().toString()).replace("%environment%", config.getEnvironment().toString()) )
				.collect(Collectors.toList());
				builder.lore(description);
			} else {
				builder = new ItemBuilder(Material.RED_WOOL);
				builder.lore(Language.GUI_MAIN_DESCRIPTION_DISABLED.getDescriptionArray());
			}
			
			builder.name("§f" + config.getWorldName());
			
			items.add(ClickableItem.of(builder.build(), click -> Menus.WORLD_SETTINGS.open(player, new String[] { "config" }, new Object[] { config })));
		}
		
		ClickableItem[] c = new ClickableItem[items.size()];
		c = items.toArray(c);
		
		pagination.setItems(c);
		pagination.setItemsPerPage(8);
		
		content.set(3, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).name(Language.GUI_GENERAL_NEXT.toString()).build(), e -> Menus.WORLD_MAIN_MENU.open(player, pagination.next().getPage())));
		content.set(3, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).name(Language.GUI_GENERAL_BACK.toString()).build(), e -> Menus.WORLD_MAIN_MENU.open(player, pagination.previous().getPage())));
		
		
		// Neue Welt erstellen
		content.set(3, 4, ClickableItem.of(new ItemBuilder(Material.EMERALD).name(Language.GUI_GENERAL_NEWWORLD.toString()).build(), click -> {
			player.closeInventory();
			player.sendMessage(Language.GUI_CHATPROMOT_ENTERWORLDNAME.toChatString());
			ChatStringInput.addPlayer(player, result -> {
				if (Bukkit.getWorld(result) != null) {
					player.sendMessage(Language.ERROR_ALREADYEXIST.toChatString());
					return;
				} else if (WorldManagerPlugin.getInstance().getWorldManager().hasWorldConfig(result)) {
					WorldConfiguration config = WorldManagerPlugin.getInstance().getWorldManager().getWorldConfig(result);
					Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> WorldManagerPlugin.getInstance().getWorldManager().loadExistingWorld(config, true));
					player.sendMessage(Language.GUI_CHATPROMOT_EXISTS.toChatString());
				} else {
					Menus.ENV_MENU.open(player, new String[] { "worldname" }, new String[] { result });
				}
			});
		}));
		
		pagination.addToIterator(content.newIterator(Type.HORIZONTAL, 0, 0));
	}

	@Override
	public void update(Player player, InventoryContents content) {
	}
}
