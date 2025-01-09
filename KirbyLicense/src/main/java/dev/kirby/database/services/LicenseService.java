package dev.kirby.database.services;

import com.j256.ormlite.dao.Dao;
import dev.kirby.checker.LoginChecker;
import dev.kirby.checker.hwid.SecureGenerator;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.entities.UsedIp;
import dev.kirby.packet.LoginPacket;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LicenseService extends DatabaseService<LicenseEntity, String> {

    public LicenseService(Dao<LicenseEntity, String> dao) {
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

    public LicenseEntity findByData(SecureGenerator generator, LoginPacket packet) throws SQLException {
        String licenseId = generator.generateSecureID(packet.getClientData(), packet.getServiceData(), new String[]{packet.getLicenseKey()});

        LicenseEntity license = findById(licenseId);

        if (LoginChecker.DEBUG) {
            System.out.println("DEBUG: " + licenseId);
            getDao().queryForAll().forEach(System.out::println);
        }

        return license;
    }
}
