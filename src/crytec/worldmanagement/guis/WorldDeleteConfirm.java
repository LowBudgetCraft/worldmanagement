package crytec.worldmanagement.guis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import crytec.worldmanagement.Language;
import crytec.worldmanagement.WorldManager;
import crytec.worldmanagement.WorldManagerPlugin;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;

public class WorldDeleteConfirm implements InventoryProvider {

	public final ItemStack white = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).name(" ").build();
	public final ItemStack black = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(" ").build();

	@Override
	public void init(Player player, InventoryContents contents) {

		World world = contents.property("world");
		String name = world.getName();
		WorldManager manager = WorldManagerPlugin.getInstance().getWorldManager();
		
		

		contents.set(SlotPos.of(1, 6), ClickableItem.of(new ItemBuilder(Material.GREEN_WOOL).name(Language.GUI_DELETION_CONFIRM.toString()).build(), e -> {
			long start = System.currentTimeMillis();
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.unloadWorld(world, done -> {
					manager.deleteWorldConfiguration(manager.getWorldConfig(name));
					manager.deleteWorldFolder(name);
					long stop = System.currentTimeMillis();
					player.sendMessage(Language.GUI_DELETION_FEEDBACK.toChatString().replace("%time%", (stop -start) + " ms"));
				});
			});
			player.closeInventory();
			return;
		}));

		contents.set(SlotPos.of(1, 2), ClickableItem.of(new ItemBuilder(Material.RED_WOOL).name(Language.GUI_DELETION_ABORT.toString()).build(), e -> {
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
