package dev.kirby.database.services;

import com.j256.ormlite.dao.Dao;
import dev.kirby.ServerLauncher;
import dev.kirby.checker.LoginChecker;
import dev.kirby.checker.hwid.HwidCalculator;
import dev.kirby.database.entities.ClientEntity;

import java.sql.SQLException;

public class ClientService extends DatabaseService<ClientEntity, String> {

    public ClientService(Dao<ClientEntity, String> dao) {
        super(dao);
    }


    public ClientEntity findByData(ServerLauncher serverLauncher, String[] clientData) throws SQLException {
        String hwid = HwidCalculator.get(serverLauncher).calculate(clientData);
        ClientEntity client = findById(hwid);

        if (LoginChecker.DEBUG) {
            System.out.println("DEBUG: " + hwid);
            getDao().queryForAll().forEach(System.out::println);
        }

        return client;
    }
}
