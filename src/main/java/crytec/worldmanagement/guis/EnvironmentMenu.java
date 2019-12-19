package crytec.worldmanagement.guis;

import crytec.worldmanagement.Language;
import crytec.worldmanagement.utils.ItemBuilder;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

public class EnvironmentMenu implements InventoryProvider {

  public EnvironmentMenu(String worldname) {
    this.worldname = worldname;
  }

  private final String worldname;

  @Override
  public void init(Player player, InventoryContent content) {

    content.set(0, 3, ClickableItem.of(new ItemBuilder(Material.GRASS).name(Language.ENVIRONMENT_NORMAL.toString()).build(), e -> SmartInventory.builder()
        .provider(new WorldTypeMenu(Environment.NORMAL, worldname))
        .size(1, 9)
        .title(Language.GUI_TITLE_WORLDTYPE.toString())
        .build().open(player)));

    content.set(0, 4, ClickableItem.of(new ItemBuilder(Material.NETHER_BRICK).name(Language.ENVIRONMENT_NETHER.toString()).build(), e -> SmartInventory.builder()
        .provider(new WorldTypeMenu(Environment.NETHER, worldname))
        .size(1, 9)
        .title(Language.GUI_TITLE_WORLDTYPE.toString())
        .build().open(player)));

    content.set(0, 5, ClickableItem.of(new ItemBuilder(Material.END_CRYSTAL).name(Language.ENVIRONMENT_END.toString()).build(), e -> SmartInventory.builder()
        .provider(new WorldTypeMenu(Environment.THE_END, worldname))
        .size(1, 9)
        .title(Language.GUI_TITLE_WORLDTYPE.toString())
        .build().open(player)));
  }
}