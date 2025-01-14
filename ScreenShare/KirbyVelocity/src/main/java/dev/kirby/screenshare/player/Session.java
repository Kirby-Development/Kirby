package dev.kirby.screenshare.player;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Getter
@Setter
public class Session {
    private final int id;
    private final SSPlayer staff, suspect;
    private final Set<SSPlayer> debug = new HashSet<>();

    public Session(int id, SSPlayer staff, SSPlayer suspect, SSPlayer... debug) {
        this.id = id;
        this.staff = staff;
        this.suspect = suspect;
        this.debug.addAll(Arrays.asList(debug));
    }

    @Getter
    public static class Manager {
        private int id = 0;

        private final Map<Integer, Session> sessions = new ConcurrentHashMap<>();

        public Session create(SSPlayer staff, SSPlayer suspect, SSPlayer... debug) {
            Session session = new Session(id++, staff, suspect, debug);
            create(session);
            return session;
        }

        public void delete(int id) {
            sessions.remove(id);
            this.id--;
        }

        public void delete(Session session) {
            sessions.remove(session.getId());
            this.id--;
        }

        public void create(Session session) {
            sessions.put(session.getId(), session);
        }

        public Session getSession(int id) {
            if (sessions.containsKey(id))
                return sessions.get(id);
            return null;
        }

        public boolean contains(int id) {
            return sessions.containsKey(id);
        }
    }

}
