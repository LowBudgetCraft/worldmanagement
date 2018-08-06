package crytec.worldmanagement.guis;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import crytec.worldmanagement.Worldmanagement;
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
		
		for (World world : Bukkit.getWorlds()) {			
			items.add(ClickableItem.of(new ItemBuilder(Material.WHITE_WOOL).name(ChatColor.GRAY + world.getName())
					.lore("").lore("")
					.lore("�aLinksklick�7 um dich in diese Welt zu")
					.lore("�7teleportieren. �aRechtsklick�7 um die")
					.lore("�7Einstellungen zu �ffnen.")
				.build(), click -> {
				if (click.getClick() == ClickType.LEFT) {
					player.teleport(world.getSpawnLocation());
					player.sendMessage(F.main("WorldManagement", "Du wurdest an den Spawnpunkt der Welt " + F.name(world.getName()) + " teleportiert."));
				} else if (click.getClick() == ClickType.RIGHT) {
								System.out.println("Settings");
								Menus.WORLD_SETTINGS.open(player, new String[] { "world" }, new Object[] { world.getName() });
				} else return;
			}));
		}
		
		ClickableItem[] c = new ClickableItem[items.size()];
		c = items.toArray(c);
		
		pagination.setItems(c);
		pagination.setItemsPerPage(8);
		
		content.set(3, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).name("�7�lN�chste Seite").build(), e -> Menus.WORLD_MAIN_MENU.open(player, pagination.next().getPage())));
		content.set(3, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).name("�7�lSeite zur�ck").build(), e -> Menus.WORLD_MAIN_MENU.open(player, pagination.previous().getPage())));
		content.set(3, 4, ClickableItem.of(new ItemBuilder(Material.EMERALD).name("�2Neue Welt erstellen.").build(), click -> {
			player.closeInventory();
			player.sendMessage(F.main("WorldManagement", "Bitte gebe einen Weltnamen ein:"));
			ChatStringInput.addPlayer(player, result -> {
				if (!Worldmanagement.getInstance().doesWorldexsists(result)) {
					player.sendMessage(F.error("Diese Welt existiert bereits."));
					return;
				} else {
					player.sendMessage(F.main("WorldManagement", "Bitte gebe einen Generator ein, benutze " + F.name("none") + " um diesen Schritt zu �berspringen."));
					ChatStringInput.addPlayer(player, generator -> {
						if (generator.equalsIgnoreCase("none")) {
							player.sendMessage("�4>> �7�bersprungen");
						}
						Menus.ENV_MENU.open(player, new String[] {"worldname", "generator"  }, new String[] { result, generator });
					});
				}
			});			
		}));
		pagination.addToIterator(content.newIterator(Type.HORIZONTAL, 0, 0));
	}

	@Override
	public void update(Player player, InventoryContents content) {
	}
}
