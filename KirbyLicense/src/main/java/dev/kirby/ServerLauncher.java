package dev.kirby;

import dev.kirby.checker.hwid.HwidCalculator;
import dev.kirby.checker.hwid.SecureUUIDGenerator;
import dev.kirby.config.Config;
import dev.kirby.config.ConfigManager;
import dev.kirby.database.DatabaseManager;
import dev.kirby.database.entities.ClientEntity;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.entities.ResourceEntity;
import dev.kirby.packet.registry.PacketRegister;
import dev.kirby.server.NettyServer;
import dev.kirby.server.ServerEvents;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ServerLauncher implements Runnable {

    private final SecureUUIDGenerator secureUUIDGenerator;

    private final Config config;

    public ServerLauncher() throws Exception {
        configManager.loadConfig(Config.class);
        config = configManager.getConfig();
        secureUUIDGenerator = new SecureUUIDGenerator(config.getSecurityKey());

        //genTest();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    private void shutdown() {
        configManager.saveConfig();
    }

    private void genTest() throws Exception {
        ResourceEntity resource;
        ClientEntity client;
        UUID resourceId = secureUUIDGenerator.generateSecureUUID("test", "1.0");
        String[] concat = Utils.concat(Utils.getData(), "test", "1.0", "testkey");
        UUID clientData = secureUUIDGenerator.generateSecureUUID(concat);
        databaseManager.getResourceService().create(resource = new ResourceEntity(resourceId, "test"));
        databaseManager.getClientService().create(client = new ClientEntity(HwidCalculator.get(this).calculate(Utils.getData()), "127.0.0.1"));
        databaseManager.getLicenseService().create(new LicenseEntity(clientData, resource, client, configManager.getConfig()));
    }

    public static void main(String[] args) throws Exception {
        new ServerLauncher().run();
    }

    private final DatabaseManager databaseManager = new DatabaseManager("./test.db");
    private final ConfigManager<Config> configManager = new ConfigManager<>(new Config());
    private final ServerEvents serverEvents = new ServerEvents(this);

    public void run() {
        final NettyServer server = new NettyServer(PacketRegister.get().getPacketRegistry(),
                future -> System.out.println("Server running..."),
                serverEvents);
    }

}