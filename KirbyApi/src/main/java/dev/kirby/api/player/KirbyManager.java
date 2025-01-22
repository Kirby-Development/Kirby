package dev.kirby.api.player;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.kirby.api.plugin.KirbyPlugin;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class KirbyManager<T extends KirbyUser, K extends KirbyPlugin> {

    private final Map<UUID, T> profiles = new ConcurrentHashMap<>();
    protected final K plugin;

    protected KirbyManager(K plugin) {
        this.plugin = plugin;
    }

    public T createProfile(final Player player, final User user) {
        final UUID uuid = player.getUniqueId();
        if (this.profiles.containsKey(uuid)) return profiles.get(uuid);
        final T data = getData(player, user);
        this.profiles.put(uuid, data);
        return data;
    }

    protected abstract T getData(Player player, User user);

    public T removeProfile(UUID player) {
        return this.profiles.remove(player);
    }

    public T removeProfile(Player player) {
        return this.profiles.remove(player.getUniqueId());
    }

    public T getProfile(final UUID player) {
        return this.profiles.get(player);
    }

    public T getProfile(final Player player) {
        return this.profiles.get(player.getUniqueId());
    }
}
