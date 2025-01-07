package dev.kirby.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.kirby.database.entities.AllowedIpEntity;
import dev.kirby.database.entities.ClientEntity;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.services.ClientService;
import dev.kirby.database.services.IpService;
import dev.kirby.database.services.LicenseService;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.UUID;

@Getter
public class DatabaseManager {

    private final Dao<AllowedIpEntity, Integer> allowedIpEntities;
    private final Dao<ClientEntity, String> clientEntities;
    private final Dao<LicenseEntity, UUID> licenseEntities;

    private final IpService ipService;
    private final ClientService clientService;
    private final LicenseService licenseService;

    private final ConnectionSource connectionSource;

    @SneakyThrows
    public DatabaseManager(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        ipService = new IpService(allowedIpEntities = createDao(AllowedIpEntity.class));
        clientService = new ClientService(clientEntities = createDao(ClientEntity.class));
        licenseService = new LicenseService(licenseEntities = createDao(LicenseEntity.class));
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

