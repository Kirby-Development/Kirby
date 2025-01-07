package dev.kirby.database.services;

import com.j256.ormlite.dao.Dao;
import dev.kirby.database.entities.ClientEntity;

public class ClientService extends DatabaseService<ClientEntity, String> {

    public ClientService(Dao<ClientEntity, String> dao) {
        super(dao);
    }

}
