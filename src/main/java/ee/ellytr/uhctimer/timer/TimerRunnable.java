package ee.ellytr.uhctimer.timer;

import ee.ellytr.uhctimer.Timer;
import ee.ellytr.uhctimer.utils.MessageUtils;
import ee.ellytr.uhctimer.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TimerRunnable implements Runnable {

	private String message;
	private double time, running, start, nextSound, nextChat;
	private boolean cancelled;

	@Override
	public void run() {
		FileConfiguration config = Timer.getInstance().getConfig();
		running = (System.currentTimeMillis() - start) / 1000.0;
		if (!cancelled) {
			double remaining = time - running;
			if (remaining < 0) {
				cancelled = true;
				Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Timer" + ChatColor.DARK_GREEN + "] " + ChatColor.valueOf(config.getString("description-color").toUpperCase().replaceAll(" ", "_")) + message + "!");
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 2);
					if (!config.contains("hidden-timer-players") || !config.getStringList("hidden-timer-players").contains(player.getUniqueId().toString())) {
						MessageUtils.sendActionBar(player, new ComponentBuilder(message + ": ").color(ChatColor.valueOf(config.getString("description-color").toUpperCase().replaceAll(" ", "_"))).bold(true).append("00:00").color(ChatColor.valueOf(config.getString("time-color").toUpperCase().replaceAll(" ", "_"))).bold(false).append(config.getBoolean("time-show-millis") ? ".000" : "").create());
					}
				}
			} else {
				if (nextChat >= remaining && nextChat != 0) {
					if (nextChat <= 5) {
						Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Timer" + ChatColor.DARK_GREEN + "] " + ChatColor.valueOf(config.getString("description-color").toUpperCase().replaceAll(" ", "_")) + message + ChatColor.valueOf(config.getString("time-color").toUpperCase().replaceAll(" ", "_")) + " in " + StringUtils.formatExtendedTime(nextChat));
						nextChat--;
					} else if (nextChat <= 60) {
						Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Timer" + ChatColor.DARK_GREEN + "] " + ChatColor.valueOf(config.getString("description-color").toUpperCase().replaceAll(" ", "_")) + message + ChatColor.valueOf(config.getString("time-color").toUpperCase().replaceAll(" ", "_")) + " in " + StringUtils.formatExtendedTime(nextChat));
						nextChat -= 10;
					} else {
						Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Timer" + ChatColor.DARK_GREEN + "] " + ChatColor.valueOf(config.getString("description-color").toUpperCase().replaceAll(" ", "_")) + message + ChatColor.valueOf(config.getString("time-color").toUpperCase().replaceAll(" ", "_")) + " in " + StringUtils.formatExtendedTime(nextChat));
						if ((nextChat / 60) % 5 == 0 && nextChat != 300) {
							nextChat -= 300;
						} else if (nextChat % 60 == 0 && nextChat <= 300) {
							nextChat -= 60;
						} else {
							nextChat = (nextChat / 300) * 300;
						}
					}
				}
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (nextSound >= remaining) {
						player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
						nextSound --;
					}
					if (!config.contains("hidden-timer-players") || !config.getStringList("hidden-timer-players").contains(player.getUniqueId().toString())) {
						MessageUtils.sendActionBar(player, new ComponentBuilder(message + ": ").color(ChatColor.valueOf(config.getString("description-color").toUpperCase().replaceAll(" ", "_"))).bold(true).append(config.getBoolean("time-show-millis") ? StringUtils.formatTimeWithMillis(remaining) : StringUtils.formatTime(remaining)).color(ChatColor.valueOf(config.getString("time-color").toUpperCase().replaceAll(" ", "_"))).bold(false).create());
					}
				}
			}
		}
		Bukkit.getScheduler().runTaskLaterAsynchronously(Timer.getInstance(), this, 1);
	}

	public void setTime(double time) {
		this.time = time;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
		if (!cancelled) {
			running = 0;
			start = System.currentTimeMillis();
			nextSound = Timer.getInstance().getConfig().getDouble("start-countdown");
			nextChat = time;
		}
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
