package com.nguyenquyhy.discordbridge.listeners;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.hooks.Boop;
import com.nguyenquyhy.discordbridge.hooks.Nucleus;
import com.nguyenquyhy.discordbridge.models.ChannelConfig;
import com.nguyenquyhy.discordbridge.models.GlobalConfig;
import com.nguyenquyhy.discordbridge.utils.ChannelUtil;
import com.nguyenquyhy.discordbridge.utils.ErrorMessages;
import com.nguyenquyhy.discordbridge.utils.TextUtil;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Hy on 10/13/2016.
 */
public class ChatListener {
    DiscordBridge mod = DiscordBridge.getInstance();

    /**
     * Send chat from Minecraft to Discord
     *
     * @param event
     */
    @Listener(order = Order.LATE)
    public void onChat(MessageChannelEvent.Chat event) {

        if (event.isCancelled() || event.isMessageCancelled()) return;

        sendToDiscord(event);
        formatForMinecraft(event);
    }

    private void sendToDiscord(MessageChannelEvent.Chat event) {
        GlobalConfig config = mod.getConfig();

        boolean isStaffChat = false;
        if (event.getChannel().isPresent()) {
            MessageChannel channel = event.getChannel().get();
            if (channel.getClass().getName().equals(Nucleus.getStaffMessageChannelClass()))
                isStaffChat = true;
            else if(channel.getClass().getName().equals(Boop.getMessageChannelClass()))
        	isStaffChat = false;
            else if (!channel.getClass().getName().startsWith("org.spongepowered.api.text.channel.MessageChannel"))
                return; // Ignore all other types
        }

        String plainString = event.getRawMessage().toPlain().trim();
        if (StringUtils.isBlank(plainString) || plainString.startsWith("/")) return;

        plainString = TextUtil.formatMinecraftMessage(plainString);
        Optional<Player> player = event.getCause().first(Player.class);

        if (player.isPresent()) {
            UUID playerId = player.get().getUniqueId();

            DiscordAPI client = mod.getBotClient();
            boolean isBotAccount = true;
            if (mod.getHumanClients().containsKey(playerId)) {
                client = mod.getHumanClients().get(playerId);
                isBotAccount = false;
            }

            if (client != null) {
                for (ChannelConfig channelConfig : config.channels) {
                    if (StringUtils.isNotBlank(channelConfig.discordId) && channelConfig.discord != null) {
                        String template = null;
                        if (!isStaffChat && channelConfig.discord.publicChat != null) {
                            template = isBotAccount ? channelConfig.discord.publicChat.anonymousChatTemplate : channelConfig.discord.publicChat.authenticatedChatTemplate;
                        } else if (isStaffChat && channelConfig.discord.staffChat != null) {
                            template = isBotAccount ? channelConfig.discord.staffChat.anonymousChatTemplate : channelConfig.discord.staffChat.authenticatedChatTemplate;
                        }

                        if (StringUtils.isNotBlank(template)) {
                            Channel channel = client.getChannelById(channelConfig.discordId);

                            if (channel == null) {
                                ErrorMessages.CHANNEL_NOT_FOUND.log(channelConfig.discordId);
                                return;
                            }

                            // Format Mentions for Discord
                            plainString = TextUtil.formatMinecraftMention(plainString, channel.getServer(), player.get(), isBotAccount);

                            if (isBotAccount) {
//                                if (channel == null) {
//                                    LoginHandler.loginBotAccount();
//                                }
                                String content = String.format(
                                        template.replace("%a",
                                                TextUtil.escapeForDiscord(player.get().getName(), template, "%a")),
                                        plainString);
                                ChannelUtil.sendMessage(channel, content);
                            } else {
//                                if (channel == null) {
//                                    LoginHandler.loginHumanAccount(player.get());
//                                }
                                ChannelUtil.sendMessage(channel, String.format(template, plainString));
                            }
                        }
                    }
                }
            }
        }
    }

    private void formatForMinecraft(MessageChannelEvent.Chat event) {
        Text rawMessage = event.getRawMessage();
        Optional<Player> player = event.getCause().first(Player.class);

        if (player.isPresent()) {
/*            UUID playerId = player.get().getUniqueId();

            for (ChannelConfig channelConfig : config.channels) {
                String template = null;

                Channel channel = client.getChannelById(channelConfig.discordId);

                Optional<User> userOptional = DiscordUtil.getUserByName(player.get().getName(), channel.getServer());
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                }

                ChannelMinecraftConfigCore minecraftConfig = channelConfig.minecraft;
                if (channelConfig.minecraft.roles != null) {
                    Collection<Role> roles = message.getAuthor().getRoles(message.getChannelReceiver().getServer());
                    for (String roleName : channelConfig.minecraft.roles.keySet()) {
                        if (roles.stream().anyMatch(r -> r.getName().equals(roleName))) {
                            ChannelMinecraftConfigCore roleConfig = channelConfig.minecraft.roles.get(roleName);
                            roleConfig.inherit(channelConfig.minecraft);
                            minecraftConfig = roleConfig;
                            break;
                        }
                    }
                }
            }

            event.setMessage(rawMessage);*/
        }

    }
}