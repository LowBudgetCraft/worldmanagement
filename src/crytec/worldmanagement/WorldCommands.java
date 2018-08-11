package crytec.worldmanagement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import crytec.worldmanagement.guis.Menus;

public class WorldCommands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length == 0) {
			if (sender instanceof Player) {

				Player p = (Player) sender;
				if (p.hasPermission("worldmanagement.admin")) {
					Menus.WORLD_MAIN_MENU.open(p);
					return true;
				} else {
					p.sendMessage("§cDu hast keine Berechtigung diesen Command zu nutzen!");
					return true;
				}

			} else {
				sender.sendMessage("§cDieser Command kann nur von einem Spieler ausgeführt werden.");
				return true;
			}
		} else {
			return false;
		}
	}
}