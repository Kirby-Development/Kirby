package dev.kirby.api.service;

public interface Provider<T> {
    T get();
}