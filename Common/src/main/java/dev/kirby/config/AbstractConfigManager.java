package dev.kirby.config;

import lombok.Getter;
import lombok.Setter;

import java.io.*;

public abstract class AbstractConfigManager<T> {

    private final File file;

    @Setter
    @Getter
    private T config;

    public AbstractConfigManager(final T defaultConfig) {
        this.config = defaultConfig;
        String name = defaultConfig.getClass().getSimpleName().toLowerCase();
        this.file = new File(name + "." + extension());
    }

    public AbstractConfigManager(final T defaultConfig, final String name) {
        this.config = defaultConfig;
        this.file = new File(name + "." + extension());
    }

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(file)) {
            save(config, writer);
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
            this.config = load(reader, configClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract String extension();

    protected abstract void save(T config, FileWriter writer);
    protected abstract T load(FileReader reader, Class<T> configClass);

}
