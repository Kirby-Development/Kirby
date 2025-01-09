package dev.kirby.api.service;

import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Registry {
    private static final Map<String, KirbyInstance<? extends KirbyPlugin>> INSTANCES = new ConcurrentHashMap<>();
    private static final Map<String, KirbyPlugin> PLUGINS = new ConcurrentHashMap<>();

    public void install(KirbyPlugin plugin) {
        PLUGINS.put(plugin.getName(), plugin);
    }

    public KirbyPlugin getPlugin(String name) {
        return PLUGINS.get(name);
    }

    public void unregister(KirbyPlugin plugin) {
        PLUGINS.remove(plugin.getName(), plugin);
    }

    public void install(KirbyInstance<? extends KirbyPlugin> instance) {
        INSTANCES.put(instance.getName(), instance);
    }

    public KirbyInstance<? extends KirbyPlugin> getInstance(String name) {
        return INSTANCES.get(name);
    }

    public void unregister(KirbyInstance<? extends KirbyPlugin> instance) {
        INSTANCES.remove(instance.getName(), instance);
    }
}