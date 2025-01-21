package dev.kirby.config.impl;

import com.google.gson.Gson;
import dev.kirby.config.AbstractConfigManager;

import java.io.FileReader;
import java.io.FileWriter;

public class JsonConfigManager<T> extends AbstractConfigManager<T> {

    public static final Gson GSON = new Gson().newBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public JsonConfigManager(T defaultConfig) {
        super(defaultConfig);
    }

    public JsonConfigManager(T defaultConfig, String name) {
        super(defaultConfig, name);
    }


    @Override
    protected String extension() {
        return "json";
    }

    @Override
    protected void save(T config, FileWriter writer) {
        GSON.toJson(config, writer);
    }

    @Override
    protected T load(FileReader reader, Class<T> configClass) {
        return GSON.fromJson(reader, configClass);
    }

}
