package dev.kirby;

import dev.kirby.checker.hwid.HwidCalculator;
import dev.kirby.checker.hwid.SecureGenerator;
import dev.kirby.config.Config;
import dev.kirby.config.ConfigManager;
import dev.kirby.database.DatabaseManager;
import dev.kirby.database.entities.ClientEntity;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.entities.ResourceEntity;
import dev.kirby.packet.PingPacket;
import dev.kirby.packet.registry.PacketRegister;
import dev.kirby.server.NettyServer;
import dev.kirby.server.ServerEvents;
import dev.kirby.service.ServiceHelper;
import dev.kirby.service.ServiceManager;
import dev.kirby.service.ServiceRegistry;
import dev.kirby.thread.ThreadManager;
import io.netty.channel.Channel;
import lombok.Getter;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class ServerLauncher implements Runnable, ServiceHelper {

    private final ConfigManager<Config> configManager = new ConfigManager<>(new Config());
    private final DatabaseManager databaseManager;
    private final ServerEvents serverEvents = new ServerEvents(this);
    private final SecureGenerator generator;
    private final Config config;

    private final ThreadManager threadManager = new ThreadManager();

    public ServerLauncher() throws Exception {
        configManager.loadConfig(Config.class);
        config = configManager.getConfig();
        generator = new SecureGenerator(config.getSecurityKey());

        Config.Database database = config.getDatabase();

        databaseManager = switch (database.getMode()) {
            case SQLite -> new DatabaseManager(String.format("./%s.db", database.getDatabase()));
            case null, default ->
                    new DatabaseManager(database.getHost(), database.getPort(), database.getDatabase(), "", database.getUsername(), database.getPassword());
        };


        //genTest();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public static void main(String[] args) throws Exception {
        ServerLauncher launcher = new ServerLauncher();
        launcher.run();
    }

    private void genTest() throws Exception {
        ClientEntity client;
        databaseManager.getClientService().create(client = new ClientEntity(HwidCalculator.get(this).calculate(Utils.getData()), "127.0.0.1"));

        for (String name : List.of("KirbyApi", "Kirby", "KirbyScreenShare")) {
            String[] data = new String[]{name, "1.0"};
            ResourceEntity resource = getResource(data);
            String licenseId = generator.generateSecureID(Utils.getData(), data, new String[]{"LUCKY"});
            databaseManager.getResourceService().create(resource);
            databaseManager.getLicenseService().create(new LicenseEntity(licenseId, resource, client, configManager.getConfig()));
        }
    }

    public ResourceEntity getResource(String[] data) throws Exception {
        String id = generator.generateSecureID(data);
        return new ResourceEntity(id, data[0]);
    }

    public void run() {
        final NettyServer server = new NettyServer(PacketRegister.get(), c -> {
            final AtomicReference<Channel> channel = new AtomicReference<>(c);
            threadManager.getAvailableProfileThread().execute(() -> {
                SecureRandom random = new SecureRandom();
                while (true) {
                    try {
                        long sleepTime = 5 + random.nextInt(16);
                        Thread.sleep(Duration.ofMinutes(sleepTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    channel.get().writeAndFlush(new PingPacket());
                }
            });
        }, manager());
        server.bind(9900);
        server.getEventRegistry().registerEvents(serverEvents);
    }

    private void shutdown() {
        configManager.saveConfig();
        threadManager.shutdown();
    }

    private final ServiceManager MANAGER = new ServiceManager();

    @Override
    public ServiceRegistry manager() {
        return MANAGER;
    }
}