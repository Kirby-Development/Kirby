package dev.kirby.service;

public interface ServiceHelper {

    default <T> T get(Class<T> key) {
        return manager().get(key);
    }

    default <T> void install(Class<T> key, T service) {
        manager().put(key, service);
    }

    ServiceRegistry manager();

}
