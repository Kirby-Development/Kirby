package dev.kirby.checker;

import dev.kirby.database.DatabaseManager;
import dev.kirby.netty.Packet;
import dev.kirby.packet.registration.LoginPacket;
import dev.kirby.packet.registration.Status;

public abstract class Checker<T extends Packet> {

    protected final DatabaseManager manager;
    public Checker(DatabaseManager manager) {
        this.manager = manager;
    }

    public abstract Status check(T packet, String ip);

    public static class Debug extends Checker<LoginPacket> {

        public Debug(DatabaseManager manager) {
            super(manager);
        }

        @Override
        public Status check(LoginPacket packet, String ip) {
            System.out.println(packet);
            return Status.VALID;
        }
    }

}
