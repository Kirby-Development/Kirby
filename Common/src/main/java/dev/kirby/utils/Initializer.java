package dev.kirby.utils;

public interface Initializer {

    void init();
    default void enable(){}
    void shutdown();


}
