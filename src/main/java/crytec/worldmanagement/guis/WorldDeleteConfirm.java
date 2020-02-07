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

    contents.set(
        SlotPos.of(1, 6),
        ClickableItem.of(
            new ItemBuilder(Material.GREEN_WOOL)
                .name(Language.GUI_DELETION_CONFIRM.toString())
                .build(),
            e -> {
              long start = System.currentTimeMillis();
              Bukkit.getScheduler()
                  .runTask(
                      WorldManagerPlugin.getInstance(),
                      () ->
                          manager.unloadWorld(
                              world,
                              done -> {
                                manager.deleteWorldConfiguration(worldname);
                                manager.deleteWorldFolder(worldname);
                                long stop = System.currentTimeMillis();
                                player.sendMessage(
                                    Language.GUI_DELETION_FEEDBACK
                                        .toChatString()
                                        .replace("%time%", (stop - start) + " ms"));
                              }));
              player.closeInventory();
            }));

    contents.set(
        SlotPos.of(1, 2),
        ClickableItem.of(
            new ItemBuilder(Material.RED_WOOL).name(Language.GUI_DELETION_ABORT.toString()).build(),
            e -> player.closeInventory()));
  }
}
