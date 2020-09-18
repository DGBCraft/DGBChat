package online.dgbcraft.chat.listener;

import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import online.dgbcraft.chat.DGBChat;
import online.dgbcraft.chat.Message;

import java.util.List;

public class JoinMotdListener implements Listener {

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event) {

        List<String> motd = DGBChat.getInstance().getConfig().getJoinMotd();

        Message message = new Message("");
        message.setPlayer(event.getPlayer());
        message.setServer(event.getServer());

        if (motd != null) {
            for (String msg : motd) {
                message.setComponents(msg);
                event.getPlayer().sendMessage(message.getReplacedMessage());
            }
        }

    }
}
