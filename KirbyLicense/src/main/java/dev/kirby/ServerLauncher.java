package dev.kirby;

import dev.kirby.checker.hwid.HwidCalculator;
import dev.kirby.checker.hwid.SecureGenerator;
import dev.kirby.config.Config;
import dev.kirby.config.ConfigManager;
import dev.kirby.config.Datas;
import dev.kirby.database.DatabaseManager;
import dev.kirby.database.entities.ClientEntity;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.entities.ResourceEntity;
import dev.kirby.database.services.DatabaseService;
import dev.kirby.license.Edition;
import dev.kirby.packet.registry.PacketRegister;
import dev.kirby.resources.ClientManager;
import dev.kirby.server.NettyServer;
import dev.kirby.server.ServerEvents;
import dev.kirby.service.ServiceHelper;
import dev.kirby.service.ServiceManager;
import dev.kirby.service.ServiceRegistry;
import dev.kirby.thread.ThreadManager;
import dev.kirby.utils.Utils;
import lombok.Getter;

import java.io.File;
import java.util.List;

@Getter
public class ServerLauncher implements Runnable, ServiceHelper {

    private final File dir = new File(new File("").getAbsolutePath(), "license");

    private final ConfigManager<Config> configManager = new ConfigManager<>(dir, new Config());

    private final ConfigManager<Datas> dataManager = new ConfigManager<>(dir, new Datas());

    private final ClientManager clientManager;

    private final DatabaseManager databaseManager;
    private final ServerEvents serverEvents = new ServerEvents(this);
    private final SecureGenerator generator;
    private final Config config;

    private final ThreadManager threadManager = new ThreadManager();

    public ServerLauncher(boolean clean) throws Exception {
        configManager.load();
        config = configManager.get();

        dataManager.load();

        clientManager = new ClientManager(this);

        generator = new SecureGenerator(config.getSecurityKey());

        Config.Database database = config.getDatabase();

        databaseManager = switch (database.getMode()) {
            case SQLite -> new DatabaseManager(String.format("./%s.db", database.getDatabase()));
            case null, default ->
                    new DatabaseManager(database.getHost(), database.getPort(), database.getDatabase(), "", database.getUsername(), database.getPassword());
        };

        if (clean) {
            System.out.println("cleaning db");
            databaseManager.getServices().forEach(DatabaseService::clear);
        }

        //genData();
        refreshDb();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }


    public static void main(String[] args) throws Exception {
        ServerLauncher launcher = new ServerLauncher(args.length > 0 && args[0].equalsIgnoreCase("--clean"));
        launcher.run();
    }

    private void genData() throws Exception {
        Datas datas = dataManager.get();

        Datas.Client client;
        datas.getClients().add(client = new Datas.Client(0, Utils.getData(), "127.0.0.1"));
        List<String> kirbyApi = List.of("KirbyApi", "KirbyVelocity", "Kirby", "KirbySS");
        for (int i = 0; i < kirbyApi.size(); i++) {
            String name = kirbyApi.get(i);
            String[] data = new String[]{name, "1.0"};
            Datas.Resource resource = new Datas.Resource(i, name, data);
            datas.getResources().add(resource);
            datas.getLicenses().add(new Datas.License(client.getId(), resource.getId(), "LUCKY", Edition.Enterprise));
        }

        dataManager.save();


    }

    private void refreshDb() throws Exception {
        Datas datas = dataManager.get();

        for (Datas.Client client : datas.getClients()) {
            databaseManager.getClientService().create(new ClientEntity(HwidCalculator.get(this).calculate(client.getData()), client.getIp()));
        }


        for (Datas.Resource resource : datas.getResources()) {
            databaseManager.getResourceService().create(getResource(resource.getData()));
        }

        for (Datas.License license : datas.getLicenses()) {
            Datas.Client client = license.getClient(datas);
            Datas.Resource resource = license.getResource(datas);

            String licenseId = generator.generateSecureID(client.getData(), resource.getData(), new String[]{license.getLicense()});
            LicenseEntity lic = new LicenseEntity(licenseId,
                    getResource(resource.getData()),
                    new ClientEntity(HwidCalculator.get(this).calculate(client.getData()), client.getIp()),
                    license.getEdition(),
                    config);

            databaseManager.getLicenseService().create(lic);
        }

    }

    public ResourceEntity getResource(String[] data) throws Exception {
        String id = generator.generateSecureID(data);
        return new ResourceEntity(id, data[0]);
    }

    public void run() {
        final NettyServer server = new NettyServer(PacketRegister.get(), manager(), threadManager);
        server.bind(9900);
        server.getEventRegistry().registerEvents(serverEvents);

    }

    private void shutdown() {
        configManager.save();
        dataManager.save();
        threadManager.shutdown();
    }

    private final ServiceManager MANAGER = new ServiceManager();

    @Override
    public ServiceRegistry manager() {
        return MANAGER;
    }
}