package dev.kirby.screenshare.configuration;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class ConfigManager {
    private final HashMap<String, Configuration> configs;
    private final PluginDescription pluginDescription;
    private File folder;
    private File serverJar;
    private File pluginJar;

    public ConfigManager(PluginDescription pluginDescription) {
        this.configs = new HashMap<>();
        this.pluginDescription = pluginDescription;
        try {
            this.serverJar = new File(Plugin.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            this.pluginJar = new File(this.serverJar.getParentFile(), ((Path) pluginDescription.getSource().get()).toString());
            this.folder = new File(this.serverJar.getParentFile() + "/plugins/" + this.pluginDescription.getId());
            if (!this.folder.exists()) {
                this.folder.mkdir();
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    public Configuration get(String file) {
        return this.configs.getOrDefault(file, null);
    }

    public Configuration create(String file, String source) {
        File resourcePath = new File(this.folder + "/" + file);
        if (!resourcePath.exists()) {
            createYAML(file, source, false);
        }
        reload(file);
        return this.configs.get(file);
    }

    public Configuration create(String file) {
        return create(file, file);
    }


    public void save(String file) {
        Configuration config = get(file);
        if (config == null) {
            throw new IllegalArgumentException("The specified configuration file doesn't exist!");
        }
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(this.folder + "/" + file));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        put(file, config);
    }


    public void reload(String file) {
        if (!this.configs.containsKey(file)) {
            createYAML(file, false);
        }
        Configuration conf = load(file);
        put(file, conf);
    }


    public void reloadAll() {
        this.configs.keySet().forEach(this::reload);
    }


    private Configuration load(String file) {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.folder + "/" + file));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private void put(String file, Configuration config) {
        this.configs.put(file, config);
    }


    private void createYAML(String resourcePath, String source, boolean replace) {
        try {
            File file = new File(this.folder + "/" + resourcePath);
            if (!file.getParentFile().exists() || !file.exists()) {
                file.getParentFile().mkdir();
                if (!file.exists()) {
                    file.createNewFile();
                }
                boolean forcereplace = replace;
                if (file.length() == 0L) {
                    forcereplace = true;
                }
                if (forcereplace) {
                    Files.copy(getResourceAsStream(source), file.toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
                } else {
                    Files.copy(getResourceAsStream(source), file.toPath(), new CopyOption[0]);
                }
            }
        } catch (IOException | ClassNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void createYAML(String resourcePath, boolean replace) {
        createYAML(resourcePath, resourcePath, replace);
    }


    private InputStream getResourceAsStream(String name) throws ClassNotFoundException, URISyntaxException, IOException {
        ZipFile file = new ZipFile(this.pluginJar);
        ZipInputStream zip = new ZipInputStream(this.pluginJar.toURL().openStream());
        boolean stop = false;
        while (!stop) {
            ZipEntry e = zip.getNextEntry();
            if (e == null) {
                stop = true;
                continue;
            }
            if (e.getName().equals(name)) {
                return file.getInputStream(e);
            }
        }
        return null;
    }
}