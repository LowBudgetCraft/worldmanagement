package crytec.worldmanagement;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.crytec.api.InventoryMenuAPI.InvGUI;
import net.crytec.api.InventoryMenuAPI.InvGUI.ROWS;
import net.crytec.api.InventoryMenuAPI.MenuCloseEvent;
import net.crytec.api.InventoryMenuAPI.MenuEvent;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.util.F;

public class WorldImportInterface implements Listener {
	
	private Player player;
	private InvGUI gui;
	private int step = 0;
	private String generator = "none";
	
	private Environment env;
	private WorldType type;
	private String name;
	private boolean done = false;
	
	public WorldImportInterface(Player player, String generator, String name) {
		this.player = player;
		if (generator != null) {
			this.generator = generator;
		}
		this.name = name;
		Bukkit.getPluginManager().registerEvents(this, Worldmanagement.getInstance());
		
		gui = new InvGUI(Worldmanagement.getInstance(), player, "WorldManagement", ROWS.ROW_1);
		gui.addItem(new ItemBuilder(Material.GRASS).name("§eNORMAL").build(), 0);
		gui.addItem(new ItemBuilder(Material.NETHER_BRICK).name("§eNETHER").build(), 1);
		gui.addItem(new ItemBuilder(Material.ENDER_STONE).name("§eTHE_END").build(), 2);
		
		gui.openInventory();
	}
	
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (e.getPlayer() == this.player) {
			destroy();
		}
	}
	
	
	@EventHandler
	public void onMenuClick(MenuEvent e) {
		if (!e.getTitle().equalsIgnoreCase("WorldManagement")) { return; }
		if (e.getInventory() == gui.getInventory()) { return; }
		
		if (step == 0) {
			switch (e.getSlot()) {
			case 0: {
				env = Environment.NORMAL;
				break;
			}
			case 1: {
				env = Environment.NETHER;
				break;
			}
			case 2: {
				env = Environment.THE_END;
				break;
			}
			default:
				break;

			}
			
			player.sendMessage("Du hast " + env.name() + " ausgewählt.");
			step = 1;
			
			gui.addItem(new ItemBuilder(Material.GRASS).name(CWorldType.NORMAL.name()).build(), 0);
			gui.addItem(new ItemBuilder(Material.BEDROCK).name(CWorldType.FLAT.name()).build(), 1);
			gui.addItem(new ItemBuilder(Material.MYCEL).name(CWorldType.LARGE_BIOMES.name()).build(), 2);
			gui.addItem(new ItemBuilder(Material.DIRT).name(CWorldType.AMPLIFIED.name()).build(), 3);
			gui.addItem(new ItemBuilder(Material.BARRIER).name(CWorldType.VOID.name()).build(), 4);
			return;
		}
		
		if (step == 1) {
			switch (e.getSlot()) {
			case 0: {
				type = WorldType.NORMAL;
				break;
			}
			case 1: {
				type = WorldType.FLAT;
				break;
			}
			case 2: {
				type = WorldType.LARGE_BIOMES;
				break;
			}
			case 3: {
				type = WorldType.AMPLIFIED;
				break;
			}
			case 4: {
				type = WorldType.NORMAL;
				generator = "void";
			}
			default: break;

			}
			done = true;
			createWorld();
			return;
		}
	}
	
	@EventHandler
	public void onMenuClose(MenuCloseEvent e) {
		if (!e.getTitle().equalsIgnoreCase("WorldManagement")) { return; }
		if (e.getInventory() == gui.getInventory()) {
				if (done) { return; }
				player.sendMessage(F.error("Welt erstellung abgebrochen."));
				destroy();
				return;
		}
	}
	
	private void createWorld() {
		player.closeInventory();
		player.sendMessage(F.main("WorldManagement", "Es wird eine neue Welt erstellt:"));
		player.sendMessage(F.main("WorldManagement", "Name: " + this.name ));
		player.sendMessage(F.main("WorldManagement", "Type: " + this.type.name()));
		player.sendMessage(F.main("WorldManagement", "Umgebung: " + this.env.name()));
		player.sendMessage(F.main("WorldManagement", "Generator: " + this.generator));
		destroy();
		Worldmanagement.getInstance().createNewWorld(name, env, type, generator);
	}
	
	private void destroy() {
		HandlerList.unregisterAll(this);
	}
}