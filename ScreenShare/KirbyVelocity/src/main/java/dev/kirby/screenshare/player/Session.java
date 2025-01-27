package dev.kirby.screenshare.player;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
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

    public Collection<SSPlayer> getAll() {
        List<SSPlayer> players = new ArrayList<>();
        players.add(staff);
        players.add(suspect);
        players.addAll(debug);
        return players;
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
        }

        public void delete(Session session) {
            sessions.remove(session.getId());
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
            if (id == -1) return false;
            return sessions.containsKey(id);
        }
    }

}
