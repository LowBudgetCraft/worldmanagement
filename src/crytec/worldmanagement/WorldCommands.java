package crytec.worldmanagement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import crytec.worldmanagement.guis.Menus;

public class WorldCommands implements CommandExecutor {
	
	
//	@Command(struct = "wm", perms = "wm.admin", desc = "Öffnet das Interface")
//	public CommandResult wmInterface(Player sender) {
//		
//		sender.sendMessage(F.error("Dieses Interface wird aktuell überarbeitet. Einige Funktionen sind nicht verfügbar."));
//		Menus.WORLD_MAIN_MENU.open(sender);
//		return CommandResult.success();
//	}
//	
//
//	@Command(struct = "wm import", params = { "Welt", "Generator" }, perms = "wm.admin", desc = "Importiert eine bereits vorhandende Welt.")
//	public CommandResult wmImport(Player sender, String world, @OptionalArg("none") String generator) {
//		return CommandResult.success();
//	}
//	
//	@Command(struct = "wm unload", params = { "Welt" }, perms = "wm.admin", desc = "Deaktiviert eine Welt bis zum nächsten Serverneustart.")
//	public CommandResult wmUnload(CommandSender sender, World world) {
//
//		Worldmanagement.getInstance().unloadWorld(world.getName());
//		sender.sendMessage("Die angegebene Welt wird in kürze deaktiviert.");
//		
//		return CommandResult.success();
//	}


	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length == 0) {
			if (sender instanceof Player) {

				Player p = (Player) sender;
				if (p.hasPermission("worldmanagement.admin")) {
					Menus.WORLD_MAIN_MENU.open(p);
				}

			} else {
				sender.sendMessage("§cDieser Command kann nur von einem Spieler ausgeführt werden.");
				return true;
			}
		}

		return false;
	}
}