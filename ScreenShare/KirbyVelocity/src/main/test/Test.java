import dev.kirby.config.ConfigManager;
import dev.kirby.screenshare.configuration.Config;
import dev.kirby.utils.DiscordWebhook;
import lombok.SneakyThrows;

import java.io.File;

public class Test implements Runnable {


    public static void main(String[] args) {
        Test test = new Test();
        test.run();
    }

    //C:/Users/lupic/Desktop/Kirby
    //V:/Kirby

    ConfigManager<Config> config = new ConfigManager<>(new File(new File("").getAbsolutePath(), "test"), new Config());

    @SneakyThrows
    @Override
    public void run() {
        //config.load();

        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1333196271313489940/WY9KvlJ519AAaZbEbmyu8QcOAvaKBLWV89Z9h1VQTtmHuk9PeG2ZTTIBftX4XCpW1Hdy");

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();

        embed.addField("suca", "ciao", false);

        embed.setTitle("nigga");

        webhook.addEmbed(embed);

        webhook.setContent("suca");

        webhook.execute();
    }

}
