package online.dgbcraft.chat;

import com.google.gson.JsonSyntaxException;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Message {

    private static final DGBChat INSTANCE = DGBChat.getInstance();

    private BaseComponent[] components;
    private ProxiedPlayer player = null;
    private Server server = null;
    private String message = null;

    private boolean updated = false;
    private BaseComponent[] replacedMessage;

    public Message(String str) {
        setComponents(str);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
        updated = false;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ProxiedPlayer player) {
        this.player = player;
        updated = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        updated = false;
    }

    public BaseComponent[] getComponents() {
        return components;
    }

    public void setComponents(String str) {
        try {
            setComponents(ComponentSerializer.parse(str));
        } catch (JsonSyntaxException e) {
            setComponents(new TextComponent(str));
        }
    }

    public void setComponents(BaseComponent baseComponent) {
        setComponents(new BaseComponent[]{baseComponent});
    }

    public void setComponents(BaseComponent[] baseComponents) {
        this.components = baseComponents;
        updated = false;
    }

    public BaseComponent[] getReplacedMessage() {
        if (!updated) {
            replacedMessage = replace(components);
            updated = true;
        }
        return replacedMessage;
    }

    private BaseComponent[] replace(BaseComponent[] baseComponents) {
        for (BaseComponent baseComponent : baseComponents) {
            if (baseComponent instanceof TextComponent) {                  //内容: 文本
                ((TextComponent) baseComponent).setText(replace(((TextComponent) baseComponent).getText()));
            } else if (baseComponent instanceof TranslatableComponent) {   //内容: 已翻译文本
                if (((TranslatableComponent) baseComponent).getWith() != null) {
                    //将翻译内容变量转化为BaseComponent递归
                    ((TranslatableComponent) baseComponent).setWith(Arrays.asList(replace(
                            ((TranslatableComponent) baseComponent).getWith().toArray(new BaseComponent[0])
                    )));
                }
            }
            //子对象
            if (baseComponent.getExtra() != null) {
                //将子对象转化为BaseComponent递归
                baseComponent.setExtra(Arrays.asList(replace(
                        baseComponent.getExtra().toArray(new BaseComponent[0])
                )));
            }
            //交互事件: 点击事件
            if (baseComponent.getClickEvent() != null) {
                baseComponent.setClickEvent(
                        //替换事件中交互内容的占位符
                        new ClickEvent(
                                baseComponent.getClickEvent().getAction(),
                                replace(baseComponent.getClickEvent().getValue())
                        )
                );
            }
            //交互事件: 悬停事件-显示文本
            if (baseComponent.getHoverEvent() != null && baseComponent.getHoverEvent().getAction() == HoverEvent.Action.SHOW_TEXT) {
                List<Content> contents = baseComponent.getHoverEvent().getContents();
                for (int j = 0; j < contents.size(); j++) {
                    if (contents.get(j) instanceof Text) {
                        Object value = ((Text) contents.get(j)).getValue();
                        if (value instanceof String) {
                            contents.set(j, new Text(replace((String) value)));
                        } else if (value instanceof BaseComponent[]) {
                            //递归转换子Json文本对象
                            contents.set(j, new Text(replace((BaseComponent[]) value)));
                        }
                    }
                }
            }
        }
        return baseComponents;
    }

    private String replace(String str) {
        str = str.replace("%day%", "" + (int) Math.ceil((new Date().getTime() -
                INSTANCE.getConfig().getOpenTimestamp()) / 86400000f));
        //玩家信息
        if (player != null)
            str = str.replace("%name%", player.getName())
                    .replace("%displayName%", player.getDisplayName())
                    .replace("%uuid%", player.getUniqueId().toString());
        //服务器信息
        if (server != null)
            str = str.replace("%serverName%", server.getInfo().getName())
                    .replace("%serverAlias%", INSTANCE.getConfig().getServerAlias(server.getInfo().getName()));
        //发送信息
        if (message != null)
            str = str.replace("%message%", message);

        return str;
    }
}
