package crytec.worldmanagement.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import crytec.worldmanagement.WorldManager;
import crytec.worldmanagement.WorldManagerPlugin;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;
import net.crytec.api.util.F;

public class WorldDeleteConfirm implements InventoryProvider {

	public final ItemStack white = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).name(" ").build();
	public final ItemStack black = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(" ").build();

	@Override
	public void init(Player player, InventoryContents contents) {

		World world = contents.property("world");
		String name = world.getName();
		WorldManager manager = WorldManagerPlugin.getInstance().getWorldManager();
		
		long start = System.currentTimeMillis();
		
		player.sendMessage(F.main("Info", F.name(world.getName()) + " wird nach der Bestätigung gelöscht."));

		contents.set(SlotPos.of(1, 6), ClickableItem.of(new ItemBuilder(Material.GREEN_WOOL).name("Bestätigen").build(), e -> {
			
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.unloadWorld(world, done -> {
					manager.deleteWorldConfiguration(manager.getWorldConfig(name));
					manager.deleteWorldFolder(name);
					long stop = System.currentTimeMillis();
					player.sendMessage(F.main("WorldManagement", "Die Welt wurde erfolgreich gelöscht. (" + (stop -start) + " ms)"));
				});
			});
			player.closeInventory();
			return;
		}));

		contents.set(SlotPos.of(1, 2), ClickableItem.of(new ItemBuilder(Material.RED_WOOL).name("Abbrechen").build(), e -> {
			player.closeInventory();
			return;
		}));

	}

	@Override
	public void update(Player player, InventoryContents contents) {
		int state = contents.property("state", 0);
		contents.setProperty("state", state + 1);

		if (state % 5 != 0)
			return;

		boolean cur = contents.property("outline", true);
		contents.setProperty("outline", !cur);

		if (cur) {
			contents.fillBorders(ClickableItem.empty(white));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0.95F);
		} else {
			contents.fillBorders(ClickableItem.empty(black));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1.4F);
		}

	}

}
