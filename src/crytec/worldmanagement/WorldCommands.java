package crytec.worldmanagement;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import crytec.worldmanagement.guis.Menus;
import net.crytec.api.devin.commands.Command;
import net.crytec.api.devin.commands.CommandResult;
import net.crytec.api.devin.commands.Commandable;
import net.crytec.api.devin.commands.OptionalArg;
import net.crytec.api.util.F;

public class WorldCommands implements Commandable {
	
	
	@Command(struct = "wm", perms = "wm.admin", desc = "�ffnet das Interface")
	public CommandResult wmInterface(Player sender) {
		
		sender.sendMessage(F.error("Dieses Interface wird aktuell �berarbeitet. Einige Funktionen sind nicht verf�gbar."));
		Menus.WORLD_MAIN_MENU.open(sender);
		return CommandResult.success();
	}
	

	@Command(struct = "wm create", params = { "Welt", "Generator" }, perms = "wm.admin", desc = "Erstellt eine neue Welt.")
	public CommandResult wmCreate(Player sender, String world, @OptionalArg("none") String generator) {

		World w = Bukkit.getWorld(world.toLowerCase());
		if (w != null) {
			sender.sendMessage(F.error("Diese Welt exsistiert bereits."));
			return CommandResult.success();
		}

		new WorldImportInterface(sender, generator, world);
		return CommandResult.success();
	}

	@Command(struct = "wm import", params = { "Welt", "Generator" }, perms = "wm.admin", desc = "Importiert eine bereits vorhandende Welt.")
	public CommandResult wmImport(Player sender, String world, @OptionalArg("none") String generator) {

		World w = Bukkit.getWorld(world.toLowerCase());

		if (w != null) {
			sender.sendMessage(F.error("Diese Welt exsistiert bereits."));
			return CommandResult.success();
		}

		new WorldImportInterface(sender, generator, world);
		return CommandResult.success();
	}
	
	
	@Command(struct = "wm list", perms = "wm.list", desc = "Listet alle geladenen Welten auf.")
	public CommandResult wmList(CommandSender sender) {

		
		sender.sendMessage("�7Geladene Welten:");
		Bukkit.getWorlds().forEach(n -> sender.sendMessage("�2" + n.getName() + " (�6" + n.getEnvironment().name() + "�2) " + "- (�e" + n.getWorldType().name() + "�2)"));
		
		return CommandResult.success();
	}
	
	@Command(struct = "wm delete", params = { "Welt" }, perms = "wm.admin", desc = "L�scht eine Welt.")
	public CommandResult wmDelete(CommandSender sender, World world) {

		Worldmanagement.getInstance().deleteWorld(world, status -> {
			
			if (status) {
				sender.sendMessage("Die angegebene Welt wurde gel�scht.");
			} else {
				sender.sendMessage("Es ist etwas schief gelaufen. Die Welt konnte nicht gel�scht werden.");
			}
			
		});
		
		return CommandResult.success();
	}
	
	@Command(struct = "wm unload", params = { "Welt" }, perms = "wm.admin", desc = "Deaktiviert eine Welt bis zum n�chsten Serverneustart.")
	public CommandResult wmUnload(CommandSender sender, World world) {

		Worldmanagement.getInstance().unloadWorld(world.getName());
		sender.sendMessage("Die angegebene Welt wird in k�rze deaktiviert.");
		
		return CommandResult.success();
	}
	
	@Command(struct = "wm reload", perms = "wm.admin", desc = "L�dt die Konfiguration neu")
	public CommandResult wmReload(CommandSender sender) {
		Worldmanagement.getInstance().reloadConfig();
		return CommandResult.success();
	}
	
	@Command(struct = "wm tp", params = { "Welt" }, perms = "wm.tp", desc = "Teleportiert dich in die angegebene Welt.")
	public CommandResult wmTeleport(Player sender, World world) {

		sender.teleport(world.getSpawnLocation().clone().add(0, 2, 0));
		sender.sendMessage(F.main("WorldManagement", "Du wurdest in die Welt " + F.name(world.getName()) + " teleportiert."));
		return CommandResult.success();
	}
	
}
