package dev.kirby.service;

import dev.kirby.utils.Destroyable;
import dev.kirby.utils.Initializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServiceManager implements ServiceRegistry {

    private final AtomicBoolean destroyed = new AtomicBoolean(false);

    private final Map<ServiceKey<?>, Provider<?>> registry = new ConcurrentHashMap<>();

    @NotNull
    public Set<ServiceKey<?>> keySet() {
        if (destroyed.get()) return Set.of();
        return registry.keySet();
    }

    @NotNull
    public Set<Map.Entry<ServiceKey<?>, Provider<?>>> entrySet() {
        if (destroyed.get()) return Set.of();
        return registry.entrySet();
    }

    @Override
    public void destroy() {
        for (Provider<?> provider : registry.values()) {
            Object o = provider.get();
            if (o == null) continue;
            if (o instanceof Destroyable d) d.destroy();
            if (o instanceof Initializer i) i.shutdown();
        }
        registry.clear();
        destroyed.set(true);
    }

    @Nullable
    public <T> T getOrNull(@NotNull final ServiceKey<T> key) {
        if (destroyed.get()) return null;
        final Provider<T> provider = (Provider<T>) registry.get(key);
        return provider == null ? null : provider.get();
    }

    @Nullable
    public <T> T put(@NotNull final ServiceKey<T> key, final T service) {
        if (destroyed.get()) return null;
        return put(key, singleton(service));
    }

    @Nullable
    public <T> T put(@NotNull final ServiceKey<T> key, final Provider<T> service) {
        if (destroyed.get()) return null;
        return (T) registry.put(key, service);
    }

    @Nullable
    public <T> T putIfAbsent(@NotNull final ServiceKey<T> type, final T service) {
        if (destroyed.get()) return null;
        return putIfAbsent(type, singleton(service));
    }

    @Nullable
    public <T> T putIfAbsent(@NotNull final ServiceKey<T> key, final Provider<T> service) {
        if (destroyed.get()) return null;
        return (T) registry.putIfAbsent(key, service);
    }

    private static <T> Provider<T> singleton(final T service) {
        return () -> service;
    }
}