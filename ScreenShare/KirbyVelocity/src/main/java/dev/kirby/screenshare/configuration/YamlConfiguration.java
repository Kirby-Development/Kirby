package dev.kirby.screenshare.configuration;

import com.google.common.base.Charsets;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlConfiguration
        extends ConfigurationProvider {
    private final ThreadLocal<Yaml> yaml = ThreadLocal.withInitial(() -> {
        Representer representer = new Representer();


        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        return new Yaml(new Constructor(), representer, options);
    });


    public void save(Configuration config, File file) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)) {
            save(config, writer);
        }
    }


    public void save(Configuration config, Writer writer) {
        this.yaml.get().dump(config.self, writer);
    }


    public Configuration load(File file) throws IOException {
        return load(file, null);
    }


    public Configuration load(File file, Configuration defaults) throws IOException {
        try (FileInputStream is = new FileInputStream(file)) {
            return load(is, defaults);
        }
    }


    public Configuration load(Reader reader) {
        return load(reader, null);
    }


    public Configuration load(Reader reader, Configuration defaults) {
        Map<String, Object> map = (Map<String, Object>) this.yaml.get().loadAs(reader, LinkedHashMap.class);
        if (map == null) map = new LinkedHashMap<>();

        return new Configuration(map, defaults);
    }


    public Configuration load(InputStream is) {
        return load(is, null);
    }


    public Configuration load(InputStream is, Configuration defaults) {
        Map<String, Object> map = (Map<String, Object>) this.yaml.get().loadAs(is, LinkedHashMap.class);
        if (map == null) map = new LinkedHashMap<>();
        return new Configuration(map, defaults);
    }


    public Configuration load(String string) {
        return load(string, null);
    }


    public Configuration load(String string, Configuration defaults) {
        Map<String, Object> map = (Map<String, Object>) this.yaml.get().loadAs(string, LinkedHashMap.class);
        if (map == null) map = new LinkedHashMap<>();
        return new Configuration(map, defaults);
    }
}
