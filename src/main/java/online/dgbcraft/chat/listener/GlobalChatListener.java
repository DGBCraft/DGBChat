package online.dgbcraft.chat.listener;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import online.dgbcraft.chat.DGBChat;
import online.dgbcraft.chat.Message;

import java.util.Collection;

public class GlobalChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.getMessage().startsWith("/")) return;

        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
            Message message = new Message(DGBChat.getInstance().getConfig().getGlobalChatFormat());

            message.setPlayer(sender);
            message.setServer(sender.getServer());
            message.setMessage(event.getMessage());

            Collection<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers();
            for (ProxiedPlayer player : players) {
                if (sender.getServer() != player.getServer()) {
                    player.sendMessage(message.getReplacedMessage());
                }
            }
        }
    }
}
