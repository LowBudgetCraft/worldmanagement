package crytec.worldmanagement;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;

import net.crytec.api.InventoryMenuAPI.InvGUI;
import net.crytec.api.InventoryMenuAPI.InvGUI.ROWS;
import net.crytec.api.InventoryMenuAPI.MenuEvent;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.util.ChatStringInput;
import net.crytec.api.util.F;

public class MenuListener implements Listener {

	private Worldmanagement plugin;
	
	public MenuListener(Worldmanagement plugin) {
		this.plugin = plugin;
	}
	
	/*
	 * 
	 * 		World w = Bukkit.getWorld(world.toLowerCase());
		if (w != null) {
			sender.sendMessage(F.error("Diese Welt exsistiert bereits."));
			return CommandResult.success();
		}

		new WorldImportInterface(sender, generator, world);
	 * 
	 */
	
	@EventHandler
	public void onMenuInteract(MenuEvent e) {
		if (!e.getTitle().equals("World Management")) return;
		
		if (e.getSlot() == 27) {
			e.getPlayer().closeInventory();
			
			e.getPlayer().sendTitle("§aInfo", "Tippe einen Weltnamen ein.", 20, 800, 20);
			
			ChatStringInput.input.addPlayer(e.getPlayer(), worldname -> {
				World w = Bukkit.getWorld(worldname.toLowerCase());
				if (w != null) {
					e.getPlayer().sendMessage(F.error("Diese Welt exsistiert bereits."));
					e.getPlayer().resetTitle();
					return;
				} else {
					e.getPlayer().sendTitle("§aInfo", "§7Tippe einen Weltgenerator ein, '§6none' §7für keinen.", 20, 800, 20);
					ChatStringInput.input.addPlayer(e.getPlayer(), generator -> {
						new WorldImportInterface(e.getPlayer(), generator, worldname);
						e.getPlayer().resetTitle();
					});
				}
			});
			return;
		}
		
		
		
		if (e.getClickType() == ClickType.LEFT) {
			e.getPlayer().closeInventory();
			e.setSoundCancelled(true);
			
			World world = Bukkit.getWorld(e.getItemName());
			if (world == null) return;
			
			e.getPlayer().teleport(world.getSpawnLocation());
			return;
		} else {
			
			InvGUI gui = new InvGUI(plugin, e.getPlayer(), "Welt Einstellungen", ROWS.ROW_1);
			
			gui.addItem(new ItemBuilder(Material.SIGN).name("§aSpawnpunkt setzen")
					.lore("§7Setzt den Spawnpunkt der Welt an deine")
					.lore("§7aktuelle Position.")
					.lore("")
					.build(), 0);
			
			
			gui.addItem(new ItemBuilder(Material.BED).name("§cWelt deaktivieren")
					.lore("§7Dies deaktiviert die ausgewählte Welt")
					.lore("§7bis zum nächsten neustart des Servers.")
					.lore("")
					.build(), 6);
			
			gui.addItem(new ItemBuilder(Material.BARRIER).name("§cWelt LÖSCHEN")
					.lore("§7Dies §4§llöscht§7 die ausgewählte Welt permanent")
					.lore("§7vom Server. Dieser Vorgang kann nicht")
					.lore("§7rückgängig gemacht werden!")
					.lore("")
					.build(), 8);
			
			gui.openInventory();
		}
		
		
	}
	
}
