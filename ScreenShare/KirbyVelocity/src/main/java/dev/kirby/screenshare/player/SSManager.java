package dev.kirby.screenshare.player;

import com.velocitypowered.api.proxy.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SSManager {

    private final Map<UUID, SSPlayer> profiles = new ConcurrentHashMap<>();

    public SSPlayer createProfile(final Player player) {
        final UUID uuid = player.getUniqueId();
        if (this.profiles.containsKey(uuid)) return profiles.get(uuid);
        final SSPlayer data = new SSPlayer(player);
        this.profiles.put(uuid, data);
        return data;
    }

    public Collection<SSPlayer> getProfiles() {
        return profiles.values();
    }

    public SSPlayer removeProfile(UUID player) {
        return this.profiles.remove(player);
    }

    public SSPlayer removeProfile(Player player) {
        return this.profiles.remove(player.getUniqueId());
    }

    public SSPlayer getProfile(final UUID player) {
        return this.profiles.get(player);
    }

    public SSPlayer getProfile(final Player player) {
        return this.profiles.get(player.getUniqueId());
    }

}
