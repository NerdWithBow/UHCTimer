package ee.ellytr.uhctimer.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static void sendErrorMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.RED + message);
    }

    /**
     * @author Kevin Phoenix
     */
    public static void sendActionBar(Player player, BaseComponent[] component) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{'text': '" + (component == null ? null : BaseComponent.toLegacyText(component)) + "'}"), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
