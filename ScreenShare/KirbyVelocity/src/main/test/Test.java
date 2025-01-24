import dev.kirby.config.ConfigManager;
import dev.kirby.screenshare.configuration.Config;

import java.io.File;

public class Test implements Runnable {


    public static void main(String[] args) {
        Test test = new Test();
        test.run();
    }

    //C:/Users/lupic/Desktop/Kirby
    //V:/Kirby

    ConfigManager<Config> config = new ConfigManager<>(new File(new File("").getAbsolutePath(), "test"), new Config());

    @Override
    public void run() {
        config.load();


    }

}
