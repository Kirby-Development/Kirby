package dev.kirby.config.impl;

import dev.kirby.config.AbstractConfigManager;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.FileWriter;

public class YAMLConfigManager<T> extends AbstractConfigManager<T> {

    private static final DumperOptions OPTIONS = new DumperOptions();
    private static final Yaml YAML;
    
    static {
        OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        OPTIONS.setPrettyFlow(true);
        YAML = new Yaml(OPTIONS);
    }

    public YAMLConfigManager(T defaultConfig) {
        super(defaultConfig);
    }

    public YAMLConfigManager(T defaultConfig, String name) {
        super(defaultConfig, name);
    }

    @Override
    protected String extension() {
        return "yml";
    }

    @Override
    protected void save(T config, FileWriter writer) {
        YAML.dump(config, writer);
    }

    @Override
    protected T load(FileReader reader, Class<T> configClass) {
        return YAML.loadAs(reader, configClass);
    }
}