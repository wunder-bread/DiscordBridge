package com.nguyenquyhy.discordbridge.models;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Hy on 10/13/2016.
 */
@ConfigSerializable
public class ChannelDiscordConfig {
    /**
     * This is called only when the config file is first created.
     */
    void initializeDefault() {
        joinedTemplate = "_%s just joined the server_";
        leftTemplate = "_%s just left the server_";
        descriptionTemplate ="Players online: %pc";
        publicChat = new SpongeChannelConfig();
        publicChat.authenticatedChatTemplate = "%s";
        publicChat.anonymousChatTemplate = "`%a:` %s";
        serverUpMessage = "Server has started.";
        serverDownMessage = "Server has stopped.";
        broadcastTemplate = "_<BROADCAST> %s_";
        deathTemplate = "**%s**";
    }

    @Setting
    public String joinedTemplate;
    @Setting
    public String leftTemplate;
    @Setting 
    public String descriptionTemplate;
    @Setting
    public SpongeChannelConfig publicChat;
    @Setting
    public SpongeChannelConfig staffChat;
    @Setting
    public String serverUpMessage;
    @Setting
    public String serverDownMessage;
    @Setting
    public String broadcastTemplate;
    @Setting
    public String deathTemplate;


    @Deprecated
    @Setting
    public String anonymousChatTemplate;
    @Deprecated
    @Setting
    public String authenticatedChatTemplate;

    void migrate() {
        if (StringUtils.isNotBlank(anonymousChatTemplate)) {
            if (publicChat == null) publicChat = new SpongeChannelConfig();
            publicChat.anonymousChatTemplate = anonymousChatTemplate;
            anonymousChatTemplate = null;
        }
        if (StringUtils.isNotBlank(authenticatedChatTemplate)) {
            if (publicChat == null) publicChat = new SpongeChannelConfig();
            publicChat.authenticatedChatTemplate = authenticatedChatTemplate;
            authenticatedChatTemplate = null;
        }
    }
}
