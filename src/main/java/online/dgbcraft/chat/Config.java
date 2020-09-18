package online.dgbcraft.chat;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    private final File CONFIG_FILE;

    private long openTimestamp = 0;
    private HashMap<String, String> serverAlias;

    private boolean enableGlobalChat = false;
    private String globalChatFormat = "[%serverName%]<%name%> %message%";
    private boolean enableJoinMotd = false;
    private List<String> joinMotd = null;


    protected Config(File folder) {

        if (!folder.exists())
            folder.mkdir();

        CONFIG_FILE = new File(folder, "config.yml");

        if (!CONFIG_FILE.exists())
            saveDefaultConfig();

        loadConfig();
    }

    private void saveDefaultConfig() {
        try (InputStream in = DGBChat.getInstance().getResourceAsStream("config.yml")) {
            Files.copy(in, CONFIG_FILE.toPath());

            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(CONFIG_FILE);
            //生成开服时间戳
            configuration.set("open-timestamp", new Date().getTime());

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, CONFIG_FILE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        try {
            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(CONFIG_FILE);

            openTimestamp = configuration.getLong("open-timestamp");
            List<String> serverAlias = configuration.getStringList("server-alias");

            this.serverAlias = new HashMap<>();
            for (String str : serverAlias) {
                String[] split = str.split(":");
                if(split.length >= 2 && split[0] != null && !split[0].isEmpty()
                        && split[1] != null && !split[1].isEmpty()) {
                    this.serverAlias.put(split[0], split[1]);
                }
            }

            enableGlobalChat = configuration.getBoolean("enable-global-chat");
            globalChatFormat = configuration.getString("global-chat-format");
            enableJoinMotd = configuration.getBoolean("enable-joinmotd");
            joinMotd = configuration.getStringList("joinmotd");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getOpenTimestamp() {
        return openTimestamp;
    }

    public List<String> getJoinMotd() {
        return joinMotd;
    }

    public String getGlobalChatFormat() {
        return globalChatFormat;
    }

    public boolean isEnableGlobalChat() {
        return enableGlobalChat;
    }

    public boolean isEnableJoinMotd() {
        return enableJoinMotd;
    }

    public Map<String, String> getServerAlias() {
        return serverAlias;
    }

    public String getServerAlias(String server) {
        return serverAlias.getOrDefault(server, server);
    }
}
