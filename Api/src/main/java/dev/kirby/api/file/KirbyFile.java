package dev.kirby.api.file;

import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class KirbyFile implements FileGetters {

    private final KirbyInstance<? extends KirbyPlugin> resource;
    protected final String fileName;
    protected final String dirName;
    private KirbyConfiguration config;
    protected final File file;
    protected final File dir;
    protected final boolean hasDir;

    protected KirbyFile(KirbyInstance<? extends KirbyPlugin> resource, String fileName, String dirName) {
        this.resource = resource;
        this.fileName = fileName;
        this.dirName = dirName + "/";
        dir = new File(resource.getDataFolder().getPath() + "/" + dirName);
        file = new File(dir, fileName + ".yml");
        hasDir = true;
    }

    public KirbyFile(KirbyInstance<? extends KirbyPlugin> resource, String fileName) {
        this.resource = resource;
        this.fileName = fileName;
        dirName = "";
        file = new File((dir = resource.getDataFolder()), fileName + ".yml");
        hasDir = false;
    }

    public void save() {
        getConfig().save(file);
    }

    public void reload() {
        save();
        load();
    }

    @SneakyThrows
    public void load() {
        if (!file.exists()) {
            file.createNewFile();
            config = KirbyConfiguration.loadConfiguration(file);
            if (hasDir && !dir.exists()) dir.mkdirs();
            String path = dirName + fileName + ".yml";
            if (resource.getResource(path) == null) {
                create();
            } else {
                resource.saveResource(path, false);
            }
        }
        config = KirbyConfiguration.loadConfiguration(file);
    }

    public void init() {
        load();
        save();
    }

    public void create() {
        throw new RuntimeException("Must override create method in " + getClass().getName());
    }

    @SneakyThrows
    public void delete() {
        file.delete();
    }

    public KirbyConfiguration getConfig() {
        if (config == null) init();
        return config;
    }


    @Override
    public Component getComponent(String path) {
        return getConfig().getComponent(path);
    }

    @Override
    public String getString(@NotNull String path) {
        return getConfig().getString(path);
    }

    @Override
    public @NotNull List<String> getStringList(@NotNull String path) {
        return getConfig().getStringList(path);
    }

    @Override
    public List<ItemStack> getItemStackList(String path) {
        return getConfig().getItemStackList(path);
    }

    @Override
    public <T extends ConfigurationSerializable> List<T> getSerializableList(String path) {
        return getConfig().getSerializableList(path);
    }
}
