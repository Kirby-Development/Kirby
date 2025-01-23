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

@Getter
@AllArgsConstructor
public enum Format {
    JSON(".json") {
        private static final Gson GSON = new Gson().newBuilder().disableHtmlEscaping().setPrettyPrinting().create();

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

        private static final Yaml yaml;

        static {

            //todo serializer cause yaml is bullshit
            DumperOptions yamlDumperOptions = new DumperOptions();
            yamlDumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            yamlDumperOptions.setIndent(2);
            yamlDumperOptions.setWidth(80);
            LoaderOptions yamlLoaderOptions = new LoaderOptions();
            yamlLoaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
            yamlLoaderOptions.setCodePointLimit(Integer.MAX_VALUE);

            Constructor constructor = new Constructor(yamlLoaderOptions);
            Representer representer = new Representer(yamlDumperOptions);
            representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);


            //constructor, representer, -  , yamlLoaderOptions
            yaml = new Yaml(yamlDumperOptions);
        }

        @SneakyThrows
        @Override
        protected <T> void save(T config, FileWriter writer) {
            yaml.dump(config, writer);
        }

        @SneakyThrows
        @Override
        protected <T> T load(FileReader reader, Class<T> configClass) {
            return yaml.loadAs(reader, configClass);
        }
    };

    private final String extension;

    protected abstract <T> void save(T config, FileWriter writer);

    protected abstract <T> T load(FileReader reader, Class<T> configClass);
}
