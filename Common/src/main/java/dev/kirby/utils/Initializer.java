package dev.kirby.utils;

public interface Initializer extends Destroyable {

    void init();
    void enable();
    void shutdown();

    @Override
    default void destroy() {
        shutdown();
    }
}
