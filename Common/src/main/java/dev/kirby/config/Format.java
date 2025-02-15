package dev.kirby.config;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum Format {
    JSON(".json") {

        private final Gson GSON = new Gson().newBuilder().disableHtmlEscaping().setPrettyPrinting().create();

        @Override
        protected <T> void save(T config, FileWriter writer) {
            GSON.toJson(config, writer);
        }

        @Override
        protected <T> T load(FileReader reader, Class<T> configClass) {
            return GSON.fromJson(reader, configClass);
        }
    },
    YAML(".yml") {

        private final Map<Class<?>, Yaml> cache = new ConcurrentHashMap<>();

        private final Function<Class<?>, Yaml> provider = (clazz -> {
            if (cache.containsKey(clazz)) return cache.get(clazz);

            DumperOptions yamlDumperOptions = new DumperOptions();
            yamlDumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            yamlDumperOptions.setIndent(2);
            yamlDumperOptions.setWidth(80);


            Representer representer = new Representer(yamlDumperOptions);
            representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            LoaderOptions yamlLoaderOptions = new LoaderOptions();
            yamlLoaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
            yamlLoaderOptions.setCodePointLimit(Integer.MAX_VALUE);

            Yaml yml = new Yaml(new Constructor(clazz, yamlLoaderOptions), representer, yamlDumperOptions, yamlLoaderOptions);
            cache.put(clazz, yml);
            return yml;
        });

        @SneakyThrows
        @Override
        protected <T> void save(T config, FileWriter writer) {
            provider.apply(config.getClass()).dump(config, writer);
        }

        @SneakyThrows
        @Override
        protected <T> T load(FileReader reader, Class<T> configClass) {
            return provider.apply(configClass).loadAs(reader, configClass);
        }
    };

    private final String extension;

    protected abstract <T> void save(T config, FileWriter writer);

    protected abstract <T> T load(FileReader reader, Class<T> configClass);
}
