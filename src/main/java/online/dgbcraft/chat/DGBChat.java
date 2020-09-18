package online.dgbcraft.chat;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import online.dgbcraft.chat.listener.GlobalChatListener;
import online.dgbcraft.chat.listener.JoinMotdListener;

public class DGBChat extends Plugin {

    private static DGBChat instance;

    public static DGBChat getInstance() {
        return instance;
    }

    private Config config;

    @Override
    public void onEnable() {
        instance = this;

        config = new Config(getDataFolder());

        if (config.isEnableGlobalChat()) {
            getProxy().getPluginManager().registerListener(this, new GlobalChatListener());
        }
        if (config.isEnableJoinMotd()) {
            getProxy().getPluginManager().registerListener(this, new JoinMotdListener());
        }

    }

    public Config getConfig() {
        return config;
    }
}
