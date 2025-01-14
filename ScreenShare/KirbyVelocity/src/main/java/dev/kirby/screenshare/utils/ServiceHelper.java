package dev.kirby.screenshare.utils;

import dev.kirby.screenshare.KirbyVelocity;

public interface ServiceHelper {

    default <T> T get(Class<T> key) {
        return KirbyVelocity.MANAGER.get(key);
    }

    default <T> void install(Class<T> key, T service) {
        KirbyVelocity.MANAGER.put(key, service);
    }
    
}
