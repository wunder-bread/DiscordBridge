package com.nguyenquyhy.discordbridge.discordcommands;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.utils.ChannelUtil;

import de.btobastian.javacord.entities.Channel;

public class KickCommand {

    
    public static void run(DiscordBridge mod, Channel channel, String content){
	ChannelUtil.kickPlayer(channel, content);
    }
}
