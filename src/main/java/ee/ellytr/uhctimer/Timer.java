package ee.ellytr.uhctimer;

import ee.ellytr.uhctimer.command.TimerCommand;
import ee.ellytr.uhctimer.timer.TimerRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Timer extends JavaPlugin {

	private static Timer instance;
	private static TimerRunnable timer;

	@Override
	public void onEnable() {
		instance = this;

		getConfig().options().copyDefaults(true);
		saveConfig();

		getCommand("uhctimer").setExecutor(new TimerCommand());
		getCommand("toggleuhctimer").setExecutor(new TimerCommand());
		getCommand("canceluhctimer").setExecutor(new TimerCommand());

		timer = new TimerRunnable();
		timer.setCancelled(true);
		Bukkit.getScheduler().runTask(this, timer);
	}

	@Override
	public void onDisable() {
		saveConfig();
	}

	public static Timer getInstance() {
		return instance;
	}

	public static TimerRunnable getTimer() {
		return timer;
	}

}
