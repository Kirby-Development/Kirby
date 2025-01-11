package dev.kirby.api.util;


import dev.kirby.api.KirbyApi;

public interface ServiceHelper {

    default <T> T get(Class<T> key) {
        return KirbyApi.getManager().get(key);
    }

    default <T> void install(Class<T> key, T service) {
        KirbyApi.getManager().put(key, service);
    }

}
