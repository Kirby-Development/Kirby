package dev.kirby;

import dev.kirby.database.DatabaseManager;
import dev.kirby.packet.registry.PacketRegister;
import dev.kirby.server.NettyServer;
import dev.kirby.server.ServerEvents;

public class Main {

    public static void main(String[] args) throws Exception {
        //todo plugin simplesqlitebrowser
        final DatabaseManager databaseManager = new DatabaseManager("./test.db");
        startServer(databaseManager);
    }

    private static void startServer(DatabaseManager databaseManager) {
        final NettyServer server = new NettyServer(PacketRegister.get().getPacketRegistry(),
                future -> System.out.println("Server running..."),
                registry -> registry.registerEvents(new ServerEvents(databaseManager)));
    }

}