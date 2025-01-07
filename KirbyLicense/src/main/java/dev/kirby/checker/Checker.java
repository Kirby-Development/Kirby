package dev.kirby.checker;

import dev.kirby.database.DatabaseManager;
import dev.kirby.netty.Packet;
import dev.kirby.packet.Status;

public abstract class Checker<T extends Packet> {

    protected final DatabaseManager manager;
    public Checker(DatabaseManager manager) {
        this.manager = manager;
    }

    public abstract Status check(T packet, String ip);

}
