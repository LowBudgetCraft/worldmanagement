package crytec.worldmanagement;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import net.crytec.api.devin.commands.CommandRegistrar;
import net.crytec.api.util.C;

public class APIHook implements Listener {

	private CommandRegistrar cr;
	
	@EventHandler
	public void onPluginLoad(PluginEnableEvent e) {
		if (e.getPlugin().getName().equals("CT-Core")) {
			Bukkit.getServer().getConsoleSender().sendMessage(C.cAqua + "Enabling CT-Core support..");
			cr = new CommandRegistrar(Worldmanagement.getInstance());
			cr.registerCommands(new WorldCommands());
			cr.registerHelpCommands();
			
			Bukkit.getPluginManager().registerEvents(new MenuListener(Worldmanagement.getInstance()), Worldmanagement.getInstance());
		}
	}
	
}
