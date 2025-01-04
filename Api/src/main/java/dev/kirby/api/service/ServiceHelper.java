package dev.kirby.api.service;


public interface ServiceHelper {
    ServiceManager MANAGER = new ServiceManager();


    default <T> T get(Class<T> key) {
        return MANAGER.get(key);
    }

    default <T> void install(Class<T> key, T service) {
        MANAGER.put(key, service);
    }

}
