package crytec.worldmanagement;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import crytec.worldmanagement.guis.WorldMainMenu;

@CommandAlias("worldmanagement|wm")
public class WorldCommands extends BaseCommand {

	@Default
	@CommandPermission("worldmanagement.admin")
	public void openGUI(Player player) {
		WorldMainMenu.WORLD_MAIN_MENU.open(player);
	}

	@Subcommand("tp|teleport|goto")
	@CommandPermission("worldmanagement.teleport")
	@CommandCompletion("@worlds")
	public void teleportToWorld(Player player, World world) {
		player.teleport(world.getSpawnLocation());
	}
	
	@Subcommand("list")
	@CommandPermission("worldmanagement.list")
	@CommandCompletion("@worlds")
	public void listWorlds(CommandIssuer issuer) {
		issuer.sendMessage(ChatColor.GRAY + "Registered worlds: ");
		WorldManagerPlugin.getInstance().getWorldManager().getWorldConfigurations().forEach(config -> {
			issuer.sendMessage( (config.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + config.getWorldName());
		});
	}
}