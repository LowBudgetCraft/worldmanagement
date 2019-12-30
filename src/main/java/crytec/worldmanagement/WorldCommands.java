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

package crytec.worldmanagement;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import crytec.worldmanagement.guis.WorldMainMenu;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandAlias("worldmanagement|wm")
public class WorldCommands extends BaseCommand {

  @Default
  @CommandPermission("worldmanagement.admin")
  public static void openGUI(Player player) {
    WorldMainMenu.WORLD_MAIN_MENU.open(player);
  }

  @Subcommand("tp|teleport|goto")
  @CommandPermission("worldmanagement.teleport")
  @CommandCompletion("@worlds")
  public static void teleportToWorld(Player player, World world) {
    player.teleport(world.getSpawnLocation());
  }

  @Subcommand("list")
  @CommandPermission("worldmanagement.list")
  @CommandCompletion("@worlds")
  public static void listWorlds(CommandIssuer issuer) {
    issuer.sendMessage(ChatColor.GRAY + "Registered worlds: ");
    WorldManagerPlugin.getInstance().getWorldManager().getWorldConfigurations().forEach(config -> issuer.sendMessage((config.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + config.getWorldName()));
  }
}