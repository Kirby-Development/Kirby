package dev.kirby.database.services;

import com.j256.ormlite.dao.Dao;
import dev.kirby.checker.LoginChecker;
import dev.kirby.checker.hwid.SecureGenerator;
import dev.kirby.database.entities.ResourceEntity;

import java.sql.SQLException;

public class ResourceService extends DatabaseService<ResourceEntity, String> {

    public ResourceService(Dao<ResourceEntity, String> dao) {
        super(dao);
    }

    public ResourceEntity findByData(SecureGenerator generator, String[] serviceData)  throws SQLException{
        String serviceId = generator.generateSecureID(serviceData);
        ResourceEntity resource = findById(serviceId);
        if (LoginChecker.DEBUG) {
            System.out.println("DEBUG: " + serviceId);
            getDao().queryForAll().forEach(System.out::println);
        }
        return resource;
    }
}
