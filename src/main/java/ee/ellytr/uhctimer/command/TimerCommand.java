package ee.ellytr.uhctimer.command;

import ee.ellytr.uhctimer.Timer;
import ee.ellytr.uhctimer.utils.MessageUtils;
import ee.ellytr.uhctimer.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TimerCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String name = cmd.getName();
		if (name.equalsIgnoreCase("uhctimer")) {
			if (sender.hasPermission("uhc.timer.command")) {
				if (args.length < 2) {
					MessageUtils.sendErrorMessage(sender, "Too few arguments.");
					return false;
				} else {
					int time = StringUtils.timeStringToSeconds(args[0]);
					String message = "";
					for (int i = 0; i < args.length; i++) {
						if (i > 0) {
							message += args[i] + " ";
						}
					}
					message = message.trim();
					Timer.getTimer().setTime(time);
					Timer.getTimer().setMessage(message);
					Timer.getTimer().setCancelled(false);
				}
			} else {
				MessageUtils.sendErrorMessage(sender, "You don't have permission.");
			}
		}
		if (name.equalsIgnoreCase("toggleuhctimer")) {
			if (sender instanceof Player) {
				List<String> toggledPlayers = Timer.getInstance().getConfig().contains("hidden-timer-players") ? Timer.getInstance().getConfig().getStringList("hidden-timer-players") : new ArrayList<>();
				String uuid = ((Player) sender).getUniqueId().toString();
				if (toggledPlayers.contains(uuid)) {
					((Player) sender).spigot().sendMessage(
							new ComponentBuilder("You can now see the timer.").color(ChatColor.GREEN).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("This only applies to the action bar.").color(ChatColor.GRAY).create())).create());
					toggledPlayers.remove(uuid);
				} else {
					((Player) sender).spigot().sendMessage(
							new ComponentBuilder("You can no longer see the timer.").color(ChatColor.RED).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
									new ComponentBuilder("This only applies to the action bar.").color(ChatColor.GRAY).create())).create());
					toggledPlayers.add(uuid);
				}
				Timer.getInstance().getConfig().set("hidden-timer-players", toggledPlayers);
			} else {
				MessageUtils.sendErrorMessage(sender, "Console does not have access to this command.");
			}
		}
		if (name.equalsIgnoreCase("canceluhctimer")) {
			if (sender.hasPermission("uhc.timer.command")) {
				Bukkit.broadcastMessage(ChatColor.GREEN + "The timer has been cancelled.");
				Timer.getTimer().setCancelled(true);
			} else {
				MessageUtils.sendErrorMessage(sender, "You don't have permission.");
			}
		}
		return true;
	}

}
