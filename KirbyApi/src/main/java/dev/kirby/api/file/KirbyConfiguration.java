package dev.kirby.api.file;

import dev.kirby.api.util.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class KirbyConfiguration extends YamlConfiguration implements FileGetters {

    public KirbyConfiguration() {
        super();
    }

    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    private final Map<String, String> messageMap = new ConcurrentHashMap<>();
    private final Map<String, List<String>> listMap = new ConcurrentHashMap<>();

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        put(cache, path, value);
        super.set(path, value);
    }

    private <T> void put(Map<String, T> cache, @NotNull String path, @Nullable T value) {
        if (value == null) cache.remove(path);
        else cache.put(path, value);
    }

    @Override
    public @Nullable Object get(@NotNull String path, @Nullable Object def) {
        if (cache.containsKey(path)) return cache.get(path);
        Object o = super.get(path, def);
        put(cache, path, o);
        return o;
    }

    public Component getComponent(String path) {
        String s = getString(path);
        if (s == null) return Component.empty();
        return Component.text(s);
    }

    public String getString(@NotNull String path) {
        if (messageMap.containsKey(path)) return messageMap.get(path);
        String message = ColorUtils.color(super.getString(path));
        put(messageMap, path, message);
        return message;
    }

    public @NotNull List<String> getStringList(@NotNull String path) {
        if (listMap.containsKey(path)) return listMap.get(path);
        List<String> l = new ArrayList<>();
        for (String s : super.getStringList(path)) l.add(ColorUtils.color(s));
        put(listMap, path, l);
        return l;
    }

    public List<ItemStack> getItemStackList(String path) {
        return getSerializableList(path);
    }

    @SuppressWarnings("unchecked")
    public <T extends ConfigurationSerializable> List<T> getSerializableList(String path) {
        List<?> list = getList(path);
        if (list == null) return new ArrayList<>(0);
        List<T> result = new ArrayList<>();
        for (Object object : list) {
            if (object instanceof ConfigurationSerializable a) {
                T t = (T) a;
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public void save(@NotNull File file) {
        try {
            super.save(file);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to save " + file, e);
        }
    }

    public static @NotNull KirbyConfiguration loadConfiguration(@NotNull File file) {
        KirbyConfiguration config = new KirbyConfiguration();
        try {
            if (!file.exists()) file.createNewFile();
            config.load(file);
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.WARNING, "Cannot load " + file, ex);
        }

        return config;
    }

    public static @NotNull KirbyConfiguration loadConfiguration(@NotNull Reader reader) {
        KirbyConfiguration config = new KirbyConfiguration();
        try {
            config.load(reader);
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.WARNING, "Cannot load " + reader, ex);
        }

        return config;
    }
}
