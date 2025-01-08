package dev.kirby;

import dev.kirby.config.Config;
import dev.kirby.config.ConfigManager;
import dev.kirby.database.DatabaseManager;
import dev.kirby.database.entities.ResourceEntity;
import dev.kirby.packet.registry.PacketRegister;
import dev.kirby.server.NettyServer;
import dev.kirby.server.ServerEvents;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class ServerLauncher implements Runnable {

    public ServerLauncher() throws SQLException, IOException {

        databaseManager.getResourceService().create(new ResourceEntity(UUID.nameUUIDFromBytes(Utils.getBytes(new String[]{"test", "1.0"})), "test"));


    }

    public static void main(String[] args) throws SQLException, IOException {
        new ServerLauncher().run();
    }

    private final DatabaseManager databaseManager = new DatabaseManager("./test.db");
    private final ConfigManager<Config> configManager = new ConfigManager<>(new Config());
    private final ServerEvents serverEvents = new ServerEvents(databaseManager);

    public void run() {
        final NettyServer server = new NettyServer(PacketRegister.get().getPacketRegistry(),
                future -> System.out.println("Server running..."),
                serverEvents);
    }

}