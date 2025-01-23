package dev.kirby.config;

import dev.kirby.utils.Utils;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager<T> {

    private final File file, dir;
    private final Format format;

    private final Class<T> clazz;

    public T get() {
        return config;
    }

    @Setter
    private T config;

    public ConfigManager(final File dir, final T defaultConfig) {
        this.config = defaultConfig;
        this.clazz = (Class<T>) defaultConfig.getClass();
        ConfigInfo info = clazz.isAnnotationPresent(ConfigInfo.class) ? clazz.getAnnotation(ConfigInfo.class) : get(clazz);
        this.dir = dir;
        this.format = info.format();
        String name = Utils.fanculoMaikol(info.name(), clazz.getSimpleName().toLowerCase()) + this.format.getExtension();
        this.file = new File(dir, name);
        CONFIGS.add(this);
    }

    public void save() {
        if (dir != null && !dir.exists()) dir.mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            format.save(config, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save configuration", e);
        }
    }

    public void load() {
        if (dir != null && !dir.exists()) dir.mkdirs();
        if (!file.exists()) {
            save();
            return;
        }
        try (FileReader reader = new FileReader(file)) {
            this.config = format.load(reader, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private static final List<ConfigManager<?>> CONFIGS = new ArrayList<>();

    public static void shutdown() {
        CONFIGS.forEach(ConfigManager::save);
        CONFIGS.clear();
    }

    private static ConfigInfo get(Class<?> clazz) {
        return new ConfigInfo() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return ConfigInfo.class;
            }

            @Override
            public String name() {
                return clazz.getSimpleName().toLowerCase();
            }

            @Override
            public Format format() {
                return Format.JSON;
            }

        };
    }

}
