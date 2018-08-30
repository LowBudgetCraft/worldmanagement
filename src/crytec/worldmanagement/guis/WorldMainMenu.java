package crytec.worldmanagement.guis;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import crytec.worldmanagement.WorldManagerPlugin;
import crytec.worldmanagement.data.WorldConfiguration;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.Pagination;
import net.crytec.api.smartInv.content.SlotIterator.Type;
import net.crytec.api.util.ChatStringInput;
import net.crytec.api.util.F;

public class WorldMainMenu implements InventoryProvider {

	@Override
	public void init(Player player, InventoryContents content) {

		Pagination pagination = content.pagination();
		ArrayList<ClickableItem> items = new ArrayList<ClickableItem>();
		
		for (WorldConfiguration config : WorldManagerPlugin.getInstance().getWorldManager().getWorldConfigurations()) {
			
			ItemBuilder builder;
			
			if (config.isEnabled()) {
				builder = new ItemBuilder(Material.GREEN_WOOL);
				builder.lore("§aKlicke §7um die");
				builder.lore("§7Einstellungen zu öffnen.");
				builder.lore("§7Welttyp: " + config.getWorldType());
				builder.lore("§7Umgebung: " + config.getEnvironment());
				builder.lore("§7Generator: " + config.getGenerator());
			} else {
				builder = new ItemBuilder(Material.RED_WOOL);
				builder.lore("§aKlicke §7um die");
				builder.lore("§7Einstellungen zu öffnen.");
				builder.lore("");
				builder.lore("§cDiese Welt ist deaktiviert,");
				builder.lore("§cDu musst die Welt erst aktivieren");
				builder.lore("§cbevor du Einstellungen änderen kannst.");
			}
			
			builder.name("§f" + config.getWorldName());
			
			items.add(ClickableItem.of(builder.build(), click -> Menus.WORLD_SETTINGS.open(player, new String[] { "config" }, new Object[] { config })));
		}
		
		ClickableItem[] c = new ClickableItem[items.size()];
		c = items.toArray(c);
		
		pagination.setItems(c);
		pagination.setItemsPerPage(8);
		
		content.set(3, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).name("§7§lNächste Seite").build(), e -> Menus.WORLD_MAIN_MENU.open(player, pagination.next().getPage())));
		content.set(3, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).name("§7§lSeite zurück").build(), e -> Menus.WORLD_MAIN_MENU.open(player, pagination.previous().getPage())));
		
		
		// Neue Welt erstellen
		content.set(3, 4, ClickableItem.of(new ItemBuilder(Material.EMERALD).name("§2Neue Welt erstellen.").build(), click -> {
			player.closeInventory();
			player.sendMessage(F.main("WorldManagement", "Bitte gebe einen Weltnamen ein:"));
			ChatStringInput.addPlayer(player, result -> {
				if (Bukkit.getWorld(result) != null) {
					player.sendMessage(F.error("Diese Welt existiert bereits."));
					return;
				} else if (WorldManagerPlugin.getInstance().getWorldManager().hasWorldConfig(result)) {
					WorldConfiguration config = WorldManagerPlugin.getInstance().getWorldManager().getWorldConfig(result);
					Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> WorldManagerPlugin.getInstance().getWorldManager().loadExistingWorld(config, true));
					player.sendMessage(F.main("WorldManagement", "Es existiert bereits eine Konfiguration. Diese Welt wird nun importiert."));
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
