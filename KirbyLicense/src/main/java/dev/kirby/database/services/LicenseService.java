package dev.kirby.database.services;

import com.j256.ormlite.dao.Dao;
import dev.kirby.database.entities.LicenseEntity;
import lombok.SneakyThrows;

import java.util.Objects;
import java.util.UUID;

public class LicenseService extends DatabaseService<LicenseEntity, UUID> {

    public LicenseService(Dao<LicenseEntity, UUID> dao) {
        super(dao);
    }

    @SneakyThrows
    public LicenseEntity getByLicense(String key) {
        return dao.queryForEq("key", key).stream().filter(Objects::nonNull).filter(licenseEntity -> licenseEntity.getKey().equals(key)).findFirst().orElse(null);
    }
}
