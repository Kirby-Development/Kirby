package dev.kirby.api.service;

import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Registry {
    private static final Map<String, KirbyInstance<?>> INSTANCES = new ConcurrentHashMap<>();
    private static final Map<String, KirbyPlugin> PLUGINS = new ConcurrentHashMap<>();

    public void install(KirbyPlugin plugin) {
        PLUGINS.put(plugin.getName(), plugin);
    }

    public KirbyPlugin getPlugin(String name) {
        return PLUGINS.get(name);
    }

    public void install(KirbyInstance<?> plugin) {
        INSTANCES.put(plugin.getName(), plugin);
    }

    public KirbyInstance<?> getInstance(String name) {
        return INSTANCES.get(name);
    }
}