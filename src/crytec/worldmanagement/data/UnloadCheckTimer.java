package crytec.worldmanagement.data;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class UnloadCheckTimer implements Runnable {

	private Consumer<Boolean> done;
	private String world;
	private BukkitTask task;
	
	public UnloadCheckTimer(JavaPlugin plugin, Consumer<Boolean> consumer, String world) {
		this.done = consumer;
		this.world = world;
		this.task = Bukkit.getScheduler().runTaskTimer(plugin, this, 1, 1L);
	}
	
	@Override
	public void run() {
		if (Bukkit.getWorld(world) == null) {
			done.accept(true);
			this.task.cancel();
		}
	}

}
