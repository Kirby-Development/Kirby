package dev.kirby.database.services;

import com.j256.ormlite.dao.Dao;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.entities.UsedIp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LicenseService extends DatabaseService<LicenseEntity, UUID> {

    public LicenseService(Dao<LicenseEntity, UUID> dao) {
        super(dao);
    }


    public boolean isValidIp(LicenseEntity license, String ip) {
        List<String> list = new ArrayList<>();
        List<UsedIp> toRemove = new ArrayList<>();
        for (UsedIp usedIp : license.getUsedIps()) {
            String ip1 = usedIp.getIp();
            if (!usedIp.getLicense().equals(license)) toRemove.add(usedIp);
            list.add(ip1);
        }
        if (!toRemove.isEmpty()) {
            license.getUsedIps().removeAll(toRemove);
            update(license);
        }
        return list.contains(ip);
    }

    public void addUsedIp(LicenseEntity license, String ip) {
        if (isValidIp(license, ip)) return;
        if (license.aboveMax()) return;
        UsedIp usedIp = new UsedIp(ip, license);
        license.getUsedIps().add(usedIp);
        update(license);
    }

}
