package dev.kirby.screenshare.player;

import dev.kirby.manager.Profile;
import dev.kirby.manager.ProfileManager;
import lombok.Getter;
import lombok.Setter;

import java.util.*;


@Getter
@Setter
public class Session extends Profile<Long> {

    private final SSPlayer staff, suspect;
    private final Set<SSPlayer> debug = new HashSet<>();

    public Session(long id, SSPlayer staff, SSPlayer suspect, SSPlayer... debug) {
        super(id);
        this.staff = staff;
        this.suspect = suspect;
        this.debug.addAll(Arrays.asList(debug));
    }

    public Collection<SSPlayer> getAll() {
        List<SSPlayer> players = new ArrayList<>();
        players.add(staff);
        players.add(suspect);
        players.addAll(debug);
        return players;
    }

    @Getter
    public static class Manager extends ProfileManager<Long, Session> {
        private long id = 0;

        public Session create(SSPlayer staff, SSPlayer suspect, SSPlayer... debug) {
            return create(new Session(id++, staff, suspect, debug));
        }

        @Override
        public boolean contains(Long id) {
            if (id == -1) return false;
            return super.contains(id);
        }
    }

}
