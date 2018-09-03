package crytec.worldmanagement;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import crytec.worldmanagement.guis.Menus;

public class WorldCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§cThis command can only be executed by a player");
			return true;
		}
		Player player = (Player) sender;

		if (args.length == 0) {
			if (player.hasPermission("worldmanagement.admin")) {
				Menus.WORLD_MAIN_MENU.open(player);
				return true;
			} else {
				player.sendMessage(Language.ERROR_NO_PERM.toChatString());
				return true;
			}
		} else if (args.length == 2 && args[0].equals("tp")) {
			if (!player.hasPermission("worldmanagement.teleport")) {
				player.sendMessage(Language.ERROR_NO_PERM.toChatString());
				return true;
			}
			World world = Bukkit.getWorld(args[1]);
			if (world == null) {
				sender.sendMessage(Language.ERROR_NO_WORLD.toChatString());
				return true;
			}
			player.teleport(world.getSpawnLocation());
			return true;

		} else {
			return true;
		}
	}
}