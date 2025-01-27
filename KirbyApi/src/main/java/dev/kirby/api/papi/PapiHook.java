package dev.kirby.api.papi;

import dev.kirby.utils.ColorUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


@Setter
@Getter
public class PapiHook extends PlaceholderExpansion {
    private boolean initialized = false;
    private final Map<String, Function<Player, Object>> requests = new ConcurrentHashMap<>();
    private final @NotNull String version;
    private final @NotNull String author;
    private final @NotNull String identifier;

    public PapiHook(@NotNull String identifier) {
        this("1.0", "SweetyDreams_", identifier);
    }

    public PapiHook(@NotNull String version, @NotNull String author, @NotNull String identifier) {
        this.version = version;
        this.author = author;
        this.identifier = identifier;
    }


    public void add(Function<Player, Object> request, String... params) {
        for (String param : params) {
            requests.put(param, request);
        }
    }

    public void add(Request... requests) {
        for (Request request : requests) {
            add(request.request(), request.params());
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull String getAuthor() {
        return author;
    }

    @Override
    public @NotNull String getVersion() {
        return version;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return new ArrayList<>(requests.keySet());
    }

    @Override
    public boolean register() {
        boolean register = super.register();
        setInitialized(register);
        return register;
    }

    @SneakyThrows
    @Override
    public @Nullable String onPlaceholderRequest(Player p, @NotNull String params) {
        Function<Player, Object> request = requests.get(params);
        if (request == null) return null;
        return ColorUtils.color(request.apply(p));
    }

    public record Request(Function<Player, Object> request, String... params) {

    }
}
