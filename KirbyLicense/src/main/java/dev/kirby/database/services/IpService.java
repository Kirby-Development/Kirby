package dev.kirby.database.services;

import com.j256.ormlite.dao.Dao;
import dev.kirby.database.entities.UsedIp;

public class IpService extends DatabaseService<UsedIp, Integer> {
    public IpService(Dao<UsedIp, Integer> dao) {
        super(dao);
    }
}
