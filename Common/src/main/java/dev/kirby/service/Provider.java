package dev.kirby.service;

public interface Provider<T> {
    T get();
}