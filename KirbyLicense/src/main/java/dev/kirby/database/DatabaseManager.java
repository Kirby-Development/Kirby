package dev.kirby.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.kirby.database.entities.ClientEntity;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.entities.ResourceEntity;
import dev.kirby.database.entities.UsedIp;
import dev.kirby.database.services.*;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DatabaseManager {

    private final List<DatabaseService<?, ?>> services = new ArrayList<>();

    private final ClientService clientService;
    private final LicenseService licenseService;
    private final ResourceService resourceService;
    private final IpService ipService;

    private final ConnectionSource connectionSource;

    @SneakyThrows
    public DatabaseManager(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        clientService = new ClientService(createDao(ClientEntity.class));
        licenseService = new LicenseService(createDao(LicenseEntity.class));
        resourceService = new ResourceService(createDao(ResourceEntity.class));
        ipService = new IpService(createDao(UsedIp.class));

        services.add(clientService);
        services.add(licenseService);
        services.add(resourceService);
        services.add(ipService);
    }

    public DatabaseManager(String path) throws SQLException {
        this(new JdbcConnectionSource("jdbc:sqlite:" + path));
    }

    public DatabaseManager(String url, String username, String password) throws SQLException {
        this(new JdbcConnectionSource(url, username, password));
    }

    public DatabaseManager(String host, String port, String database, String url, String username, String password) throws SQLException {
        this("jdbc:mysql://" + host + ":" + port + "/" + database + url, username, password);
    }

    @SneakyThrows
    public <T, ID> Dao<T, ID> createDao(Class<T> stats) {
        TableUtils.createTableIfNotExists(connectionSource, stats);
        return DaoManager.createDao(connectionSource, stats);
    }

}

