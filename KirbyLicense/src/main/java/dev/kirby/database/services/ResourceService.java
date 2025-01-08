package dev.kirby.database.services;

import com.j256.ormlite.dao.Dao;
import dev.kirby.database.entities.ResourceEntity;

import java.util.UUID;

public class ResourceService extends DatabaseService<ResourceEntity, UUID> {

    public ResourceService(Dao<ResourceEntity, UUID> dao) {
        super(dao);
    }

}
