package dev.kirby.screenshare.configuration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public abstract class ConfigurationProvider {
    private static final Map<Class<? extends ConfigurationProvider>, ConfigurationProvider> providers = new HashMap<>();

    static {
        try {
            providers.put(YamlConfiguration.class, new YamlConfiguration());
        } catch (NoClassDefFoundError noClassDefFoundError) {
        }


        try {
            providers.put(JsonConfiguration.class, new JsonConfiguration());
        } catch (NoClassDefFoundError noClassDefFoundError) {
        }
    }

    public abstract Configuration load(String paramString, Configuration paramConfiguration);

    public static ConfigurationProvider getProvider(Class<? extends ConfigurationProvider> provider) {
        return providers.get(provider);
    }

    public abstract Configuration load(String paramString);

    public abstract Configuration load(InputStream paramInputStream, Configuration paramConfiguration);

    public abstract Configuration load(InputStream paramInputStream);

    public abstract Configuration load(Reader paramReader, Configuration paramConfiguration);

    public abstract Configuration load(Reader paramReader);

    public abstract Configuration load(File paramFile, Configuration paramConfiguration) throws IOException;

    public abstract Configuration load(File paramFile) throws IOException;

    public abstract void save(Configuration paramConfiguration, Writer paramWriter);

    public abstract void save(Configuration paramConfiguration, File paramFile) throws IOException;
}
