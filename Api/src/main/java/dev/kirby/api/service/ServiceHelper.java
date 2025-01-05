package dev.kirby.api.service;


import dev.kirby.api.KirbyApi;
import dev.kirby.api.plugin.KirbyInstance;
import dev.kirby.api.plugin.KirbyPlugin;

public interface ServiceHelper {

    default <T> T get(Class<T> key) {
        return KirbyApi.getManager().get(key);
    }

    default <T> void install(Class<T> key, T service) {
        KirbyApi.getManager().put(key, service);
    }

    default KirbyInstance<?> getInstance(String name) {
        return KirbyApi.getRegistry().getInstance(name);
    }

    default void install(KirbyInstance<?> plugin) {
        KirbyApi.getRegistry().install(plugin);
    }

    default KirbyPlugin getPlugin(String name) {
        return KirbyApi.getRegistry().getPlugin(name);
    }

    default void install(KirbyPlugin plugin) {
        KirbyApi.getRegistry().install(plugin);
    }





}
