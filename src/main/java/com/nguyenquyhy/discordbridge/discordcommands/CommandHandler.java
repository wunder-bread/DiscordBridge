package com.nguyenquyhy.discordbridge.discordcommands;

import java.util.Optional;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.models.ChannelConfig;
import com.nguyenquyhy.discordbridge.models.GlobalConfig;
import com.nguyenquyhy.discordbridge.utils.ErrorMessages;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class CommandHandler {

    private static String id = "discordbridge";

    public static void handle(Optional<Player> player, String substring) {
	DiscordBridge mod = DiscordBridge.getInstance();
	mod.getLogger().info("Working");
	queueCommand(player, substring.toLowerCase().split(" "));

    }

    public static void handle(Message message) {
	DiscordBridge mod = DiscordBridge.getInstance();
	String content = message.getContent().substring(1);
	queueCommand(message.getAuthor(), message.getChannelReceiver(),mod, content.toLowerCase().split(" "));
    }

    private static void queueCommand(Optional<Player> player, String[] strings) {
	if (!(strings.length > 1)) {
	    sendMessageToSource(player, 0);
	    return;
	}

	if (strings[0].equals("ban") && permCheck(player, id + "ban")) {

	}

    }
    private static void queueCommand(User user,Channel channel,DiscordBridge mod, String[] strings) {
	if(strings[0].equals("players")) PlayerCommand.run(mod, channel);

	//if(strings[0].equals("tps")) TpsCommand.run(mod,channel);
	//else 
    }

    private static boolean permCheck(Optional<Player> player, String permission) {
	if (!player.isPresent())
	    return true;
	else if (player.get().hasPermission(permission))
	    return true;
	else
	    return false;
    }

    private static void sendMessageToSource(Optional<Player> player, int code) {
	CommandSource src;
	if (player.isPresent())
	    src = Sponge.getServer().getPlayer(player.get().getUniqueId()).get().getCommandSource().get();
	else
	    src = Sponge.getServer().getConsole();

	if (code == 0) {
	    src.sendMessage(Text.of(TextColors.RED, "Not enough arguments"));
	}

    }

    private static void runCommand(String substring) {
	DiscordBridge mod = DiscordBridge.getInstance();
	GlobalConfig config = mod.getConfig();

	DiscordAPI client = mod.getBotClient();

	
	for (ChannelConfig channelConfig : config.channels) {
	    Channel channel = client.getChannelById(channelConfig.discordId);
	    if (channel == null) {
		ErrorMessages.CHANNEL_NOT_FOUND.log(channelConfig.discordId);
		return;
	    }
	    //ChannelUtil.banPlayer(channel, substring);
	}

    }
}
