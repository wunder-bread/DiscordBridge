package com.nguyenquyhy.discordbridge.utils;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;

import java.util.Random;

/**
 * Created by Hy on 12/4/2016.
 */
public class ChannelUtil {
    public static final String SPECIAL_CHAR = "\u2062";
    public static final String BOT_RANDOM = String.valueOf(new Random().nextInt(100000));

    public static void sendMessage(Channel channel, String content) {
        channel.sendMessage(ColorUtil.removeColor(content), null, false, SPECIAL_CHAR + BOT_RANDOM, null);
    }
    public static void setDescription(Channel channel, String content){
	channel.updateTopic(content);
    }
    public static void kickPlayer(Channel channel, String content) {
	for(User user : channel.getServer().getMembers()){
	    if(user.getName().startsWith(content)){
		channel.getServer().kickUser(user);
	    }
	}
    }
    public static void banPlayer(Channel channel, String content) {
	for(User user : channel.getServer().getMembers()){
	    if(user.getName().equals(content)){
		channel.getServer().banUser(user);
	    }
	}
    }
}
