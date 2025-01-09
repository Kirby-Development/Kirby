package dev.kirby.config;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager<T> {

    public static final Gson GSON = new Gson().newBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private final File file = new File("config.json");

    @Setter
    @Getter
    private T config;

    public ConfigManager(final T defaultConfig) {
        this.config = defaultConfig;
    }

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(Class<T> configClass) {
        if (!file.exists()) {
            saveConfig();
            return;
        }
        try (FileReader reader = new FileReader(file)) {
            this.config = GSON.fromJson(reader, configClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
