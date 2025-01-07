package dev.kirby.database.services;

import com.j256.ormlite.dao.Dao;
import dev.kirby.database.entities.AllowedIpEntity;
import dev.kirby.database.entities.LicenseEntity;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Objects;

public class IpService extends DatabaseService<AllowedIpEntity, Integer> {

    public IpService(Dao<AllowedIpEntity, Integer> dao) {
        super(dao);
    }

    @SneakyThrows
    public List<AllowedIpEntity> get(String ip, LicenseEntity license) {
        return dao.queryForAll().stream()
                .filter(Objects::nonNull)
                .filter(allowedIpEntity -> allowedIpEntity.getIp().equals(ip))
                .filter(allowedIpEntity -> {
                    LicenseEntity licenseEntity = allowedIpEntity.getLicense();
                    if (licenseEntity == null) return false;
                    return licenseEntity.equals(license);
                })
                .toList();
    }
}
