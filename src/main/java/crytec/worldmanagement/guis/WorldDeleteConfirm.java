package crytec.worldmanagement.guis;

import crytec.worldmanagement.Language;
import crytec.worldmanagement.WorldManager;
import crytec.worldmanagement.WorldManagerPlugin;
import crytec.worldmanagement.utils.ItemBuilder;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.SlotPos;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldDeleteConfirm implements InventoryProvider {

  public WorldDeleteConfirm(World world) {
    manager = WorldManagerPlugin.getInstance().getWorldManager();
    this.world = world;
  }

  private final WorldManager manager;
  private final World world;

  @Override
  public void init(Player player, InventoryContent contents) {

    String worldname = world.getName();

    contents.set(SlotPos.of(1, 6), ClickableItem.of(new ItemBuilder(Material.GREEN_WOOL).name(Language.GUI_DELETION_CONFIRM.toString()).build(), e -> {
      long start = System.currentTimeMillis();
      Bukkit.getScheduler().runTask(WorldManagerPlugin.getInstance(), () -> manager.unloadWorld(world, done -> {
        manager.deleteWorldConfiguration(worldname);
        manager.deleteWorldFolder(worldname);
        long stop = System.currentTimeMillis();
        player.sendMessage(Language.GUI_DELETION_FEEDBACK.toChatString().replace("%time%", (stop - start) + " ms"));
      }));
      player.closeInventory();
    }));

    contents.set(SlotPos.of(1, 2), ClickableItem.of(new ItemBuilder(Material.RED_WOOL).name(Language.GUI_DELETION_ABORT.toString()).build(), e -> player.closeInventory()));

  }
}