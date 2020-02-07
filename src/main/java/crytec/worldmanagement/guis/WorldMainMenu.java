/*
 *
 *  * This file is part of WorldManagement, licensed under the MIT License.
 *  *
 *  *  Copyright (c) crysis992 <crysis992@gmail.com>
 *  *  Copyright (c) contributors
 *  *
 *  *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  *  of this software and associated documentation files (the "Software"), to deal
 *  *  in the Software without restriction, including without limitation the rights
 *  *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  *  copies of the Software, and to permit persons to whom the Software is
 *  *  furnished to do so, subject to the following conditions:
 *  *
 *  *  The above copyright notice and this permission notice shall be included in all
 *  *  copies or substantial portions of the Software.
 *  *
 *  *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  *  SOFTWARE.
 *
 */

package crytec.worldmanagement.guis;

import com.google.common.base.CharMatcher;
import crytec.worldmanagement.Language;
import crytec.worldmanagement.WorldManager;
import crytec.worldmanagement.WorldManagerPlugin;
import crytec.worldmanagement.data.WorldConfiguration;
import crytec.worldmanagement.utils.ItemBuilder;
import java.util.ArrayList;
import java.util.List;
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

  public static final SmartInventory WORLD_MAIN_MENU =
      SmartInventory.builder()
          .provider(new WorldMainMenu())
          .size(4)
          .title(Language.GUI_TITLE_MAIN.toString())
          .build();

  @Override
  public void init(Player player, InventoryContent content) {

    Pagination pagination = content.pagination();
    ArrayList<ClickableItem> items = new ArrayList<>();

    for (WorldConfiguration config :
        WorldManagerPlugin.getInstance().getWorldManager().getWorldConfigurations()) {

      ItemBuilder builder;

      if (config.isEnabled()) {
        builder = new ItemBuilder(Material.GREEN_WOOL);
        List<String> description = Language.GUI_MAIN_DESCRIPTION_ENABLED.getDescriptionArray();
        description =
            description.stream()
                .map(
                    line ->
                        line.replace("%worldtype%", config.getWorldType().toString())
                            .replace("%environment%", config.getEnvironment().toString()))
                .collect(Collectors.toList());
        builder.lore(description);
      } else {
        builder = new ItemBuilder(Material.RED_WOOL);
        builder.lore(Language.GUI_MAIN_DESCRIPTION_DISABLED.getDescriptionArray());
      }

      builder.name(ChatColor.WHITE + config.getWorldName());

      items.add(
          ClickableItem.of(
              builder.build(),
              click -> {
                SmartInventory.builder()
                    .provider(new WorldSettingsGUI(config))
                    .size(5)
                    .title(Language.GUI_TITLE_SETTINGS.toString())
                    .parent(WorldMainMenu.WORLD_MAIN_MENU)
                    .build()
                    .open(player);
              }));
    }

    ClickableItem[] c = new ClickableItem[items.size()];
    c = items.toArray(c);

    pagination.setItems(c);
    pagination.setItemsPerPage(8);

    content.set(
        3,
        8,
        ClickableItem.of(
            new ItemBuilder(Material.ARROW).name(Language.GUI_GENERAL_NEXT.toString()).build(),
            e -> WorldMainMenu.WORLD_MAIN_MENU.open(player, pagination.next().getPage())));
    content.set(
        3,
        0,
        ClickableItem.of(
            new ItemBuilder(Material.ARROW).name(Language.GUI_GENERAL_BACK.toString()).build(),
            e -> WorldMainMenu.WORLD_MAIN_MENU.open(player, pagination.previous().getPage())));

    // Neue Welt erstellen
    content.set(
        3,
        4,
        ClickableItem.of(
            new ItemBuilder(Material.EMERALD)
                .name(Language.GUI_GENERAL_NEWWORLD.toString())
                .build(),
            click -> {
              new ChatInput(
                  player,
                  Language.GUI_CHATPROMOT_ENTERWORLDNAME.toChatString(),
                  false,
                  result -> {
                    if (WorldManagerPlugin.getInstance()
                            .getConfig()
                            .getBoolean("alphanumeric only", true)
                        && !CharMatcher.javaLetterOrDigit()
                            .or(CharMatcher.anyOf("_-."))
                            .matchesAllOf(result)) {
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
                      SmartInventory.builder()
                          .provider(new WorldCreationGUI(player, result))
                          .size(6)
                          .title("World Creation [ " + result + "]..")
                          .build()
                          .open(player);
                    }
                  });
            }));
    pagination.addToIterator(content.newIterator(Type.HORIZONTAL, SlotPos.of(0, 0)));
  }
}
