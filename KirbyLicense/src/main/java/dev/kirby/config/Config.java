package dev.kirby.config;

import dev.kirby.utils.DiscordWebhook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigInfo(format = Format.JSON)
public class Config {

    private int maxIps = 5;
    private int maxAttempts = 3;
    private long expiryTime = 0L;
    private String securityKey = "LUCKY";

    private Webhook webhook = new Webhook();

    private Database database = new Database();


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Database {

        private Mode mode = Mode.SQLite;

        private String database = "kirby";
        private String host = "localhost";
        private String port = "3306";

        private String username = "root";
        private String password = "";

        public enum Mode {
            SQLite,
            MySQL;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Webhook {
        private String url = "https://discord.com/api/webhooks/1333196271313489940/WY9KvlJ519AAaZbEbmyu8QcOAvaKBLWV89Z9h1VQTtmHuk9PeG2ZTTIBftX4XCpW1Hdy";


        @SneakyThrows
        public void send(String content) {
            DiscordWebhook webhook = new DiscordWebhook(url);
            webhook.setContent(content);
            webhook.execute();
        }

    }
}
