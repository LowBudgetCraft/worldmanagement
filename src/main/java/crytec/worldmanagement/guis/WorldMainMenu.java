package crytec.worldmanagement.guis;

import com.google.common.base.CharMatcher;
import crytec.worldmanagement.Language;
import crytec.worldmanagement.WorldManager;
import crytec.worldmanagement.WorldManagerPlugin;
import crytec.worldmanagement.data.WorldConfiguration;
import crytec.worldmanagement.utils.ItemBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.crytec.inventoryapi.SmartInventory;
import net.crytec.inventoryapi.api.ClickableItem;
import net.crytec.inventoryapi.api.InventoryContent;
import net.crytec.inventoryapi.api.InventoryProvider;
import net.crytec.inventoryapi.api.Pagination;
import net.crytec.inventoryapi.api.SlotIterator.Type;
import net.crytec.inventoryapi.api.SlotPos;
import net.crytec.libs.commons.utils.chatinput.ChatInput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WorldMainMenu implements InventoryProvider {

  public static <T> void replaceIf(List<T> list, Predicate<T> predicate, T replacement) {
    for (int i = 0; i < list.size(); ++i) {
      if (predicate.test(list.get(i))) {
        list.set(i, replacement);
      }
    }
  }

  public static final SmartInventory WORLD_MAIN_MENU = SmartInventory.builder().provider(new WorldMainMenu()).size(4).title(Language.GUI_TITLE_MAIN.toString()).build();

  @Override
  public void init(Player player, InventoryContent content) {

    Pagination pagination = content.pagination();
    ArrayList<ClickableItem> items = new ArrayList<>();

    for (WorldConfiguration config : WorldManagerPlugin.getInstance().getWorldManager().getWorldConfigurations()) {

      ItemBuilder builder;

      if (config.isEnabled()) {
        builder = new ItemBuilder(Material.GREEN_WOOL);
        List<String> description = Language.GUI_MAIN_DESCRIPTION_ENABLED.getDescriptionArray();
        description = description.stream().map(line ->
            line.replace("%worldtype%", config.getWorldType().toString()).replace("%environment%", config.getEnvironment().toString()))
            .collect(Collectors.toList());
        builder.lore(description);
      } else {
        builder = new ItemBuilder(Material.RED_WOOL);
        builder.lore(Language.GUI_MAIN_DESCRIPTION_DISABLED.getDescriptionArray());
      }

      builder.name(ChatColor.WHITE + config.getWorldName());

      items.add(ClickableItem.of(builder.build(), click -> {

        SmartInventory.builder().provider(new WorldSettingsGUI(config))
            .size(5).title(Language.GUI_TITLE_SETTINGS.toString())
            .parent(WorldMainMenu.WORLD_MAIN_MENU)
            .build().open(player);

      }));
    }

    ClickableItem[] c = new ClickableItem[items.size()];
    c = items.toArray(c);

    pagination.setItems(c);
    pagination.setItemsPerPage(8);

    content.set(3, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).name(Language.GUI_GENERAL_NEXT.toString()).build(), e -> WorldMainMenu.WORLD_MAIN_MENU.open(player, pagination.next().getPage())));
    content.set(3, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).name(Language.GUI_GENERAL_BACK.toString()).build(), e -> WorldMainMenu.WORLD_MAIN_MENU.open(player, pagination.previous().getPage())));

    // Neue Welt erstellen
    content.set(3, 4, ClickableItem.of(new ItemBuilder(Material.EMERALD).name(Language.GUI_GENERAL_NEWWORLD.toString()).build(), click -> {

      new ChatInput(player, Language.GUI_CHATPROMOT_ENTERWORLDNAME.toChatString(), false, result -> {
        if (WorldManagerPlugin.getInstance().getConfig().getBoolean("alphanumeric only", true) && !CharMatcher.javaLetterOrDigit().or(CharMatcher.anyOf("_-.")).matchesAllOf(result)) {
          player.sendMessage(Language.ERROR_WRONG_NAME.toChatString());
          return;
        }

        WorldManager manager = WorldManagerPlugin.getInstance().getWorldManager();

        if (Bukkit.getWorld(result) != null) {
          player.sendMessage(Language.ERROR_ALREADYEXIST.toChatString());
        } else if (manager.hasWorldConfig(result)) {
          WorldConfiguration config = manager.getWorldConfig(result);
          manager.createWorld(config);
          player.sendMessage(Language.GUI_CHATPROMOT_EXISTS.toChatString());
        } else {
          SmartInventory.builder().provider(new EnvironmentMenu(result))
              .size(1)
              .title(Language.GUI_TITLE_ENVIRONMENT.toString())
              .build().open(player);
        }
      });
    }));
    pagination.addToIterator(content.newIterator(Type.HORIZONTAL, SlotPos.of(0, 0)));
  }
}
