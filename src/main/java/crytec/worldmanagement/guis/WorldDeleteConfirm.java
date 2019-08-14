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
import net.crytec.phoenix.api.inventory.ClickableItem;
import net.crytec.phoenix.api.inventory.content.InventoryContents;
import net.crytec.phoenix.api.inventory.content.InventoryProvider;
import net.crytec.phoenix.api.inventory.content.SlotPos;
import net.crytec.phoenix.api.item.ItemBuilder;

public class WorldDeleteConfirm implements InventoryProvider {

	private static final ItemStack white = new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).name(" ").build();
	private static final ItemStack black = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(" ").build();
	
	
	public WorldDeleteConfirm(World world) {
		this.manager = WorldManagerPlugin.getInstance().getWorldManager();
		this.world = world;
	}
	
	private final WorldManager manager;
	private final World world;
	
	@Override
	public void init(Player player, InventoryContents contents) {

		String worldname = this.world.getName();
		
		contents.set(SlotPos.of(1, 6), ClickableItem.of(new ItemBuilder(Material.GREEN_WOOL).name(Language.GUI_DELETION_CONFIRM.toString()).build(), e -> {
			long start = System.currentTimeMillis();
			Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> {
				manager.unloadWorld(world, done -> {
					manager.deleteWorldConfiguration(manager.getWorldConfig(worldname));
					manager.deleteWorldFolder(worldname);
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
