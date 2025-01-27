package dev.kirby.manager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ProfileManager<Id, T extends Profile<Id>> {

    protected final Map<Id, T> profiles = new ConcurrentHashMap<>();

    public Collection<T> getProfiles() {
        return profiles.values();
    }

    public T removeProfile(final Id id) {
        return this.profiles.remove(id);
    }

    public T getProfile(final Id id) {
        return this.profiles.get(id);
    }

    public T create(T profile) {
        profiles.put(profile.getId(), profile);
        return profile;
    }


    public boolean contains(Id id) {
        return profiles.containsKey(id);
    }

}
