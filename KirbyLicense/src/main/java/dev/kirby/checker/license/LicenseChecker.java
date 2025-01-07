package dev.kirby.checker.license;

import dev.kirby.checker.Checker;
import dev.kirby.database.DatabaseManager;
import dev.kirby.database.entities.AllowedIpEntity;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.services.IpService;
import dev.kirby.database.services.LicenseService;
import dev.kirby.packet.LicensePacket;
import dev.kirby.packet.Status;

import java.util.List;
import java.util.UUID;

public class LicenseChecker extends Checker<LicensePacket> {

    private LicenseChecker(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @Override
    public Status check(LicensePacket packet, String ip) {
        UUID uuid = packet.getUuid();
        LicenseService licenseService = manager.getLicenseService();
        LicenseEntity license = licenseService.findById(uuid);
        String licenseKey = packet.getLicense();
        if (license == null && (license = licenseService.getByLicense(licenseKey)) == null) return Status.NOT_FOUND;
        if (!license.getKey().equals(licenseKey)) return Status.INVALID;
        if (license.hasExpired()) return Status.EXPIRED;
        IpService ipService = manager.getIpService();
        List<AllowedIpEntity> allowedIps = ipService.get(ip, license);
        if (allowedIps.isEmpty()) return Status.INVALID_IP;
        return Status.VALID;
    }

    private static LicenseChecker INSTANCE;

    public static LicenseChecker get(DatabaseManager databaseManager) {
        if (INSTANCE == null) INSTANCE = new LicenseChecker(databaseManager);
        return INSTANCE;
    }


}
