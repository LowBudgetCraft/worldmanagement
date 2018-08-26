package crytec.worldmanagement;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import crytec.worldmanagement.guis.Menus;
import net.crytec.api.util.F;
import net.crytec.api.util.UtilPlayer;

public class WorldCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§cDieser Command kann nur von einem Spieler ausgeführt werden.");
			return true;
		}
		Player player = (Player) sender;

		if (args.length == 0) {
			if (player.hasPermission("worldmanagement.admin")) {
				Menus.WORLD_MAIN_MENU.open(player);
				return true;
			} else {
				player.sendMessage("§cDu hast keine Berechtigung diesen Command zu nutzen!");
				return true;
			}
		} else if (args.length == 2 && args[0].equals("tp")) {
			if (!player.hasPermission("worldmanagement.teleport")) {
				player.sendMessage("§cDu hast keine Berechtigung diesen Command zu nutzen!");
				return true;
			}
			World world = Bukkit.getWorld(args[1]);
			if (world == null) {
				sender.sendMessage(F.error("Diese Welt existiert nicht."));
				return true;
			}

			UtilPlayer.teleport(player, world.getSpawnLocation());
			return true;

		} else {
			return true;
		}
	}
}