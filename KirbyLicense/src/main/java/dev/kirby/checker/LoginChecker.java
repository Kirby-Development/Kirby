package dev.kirby.checker;

import dev.kirby.Utils;
import dev.kirby.checker.hwid.HwidCalculator;
import dev.kirby.database.DatabaseManager;
import dev.kirby.database.entities.ClientEntity;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.entities.ResourceEntity;
import dev.kirby.database.services.LicenseService;
import dev.kirby.packet.LoginPacket;
import dev.kirby.packet.Status;
import lombok.SneakyThrows;

import java.util.UUID;

public class LoginChecker extends Checker<LoginPacket> {

    private LoginChecker(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    private static LoginChecker INSTANCE;

    public static LoginChecker get(DatabaseManager databaseManager) {
        if (INSTANCE == null) INSTANCE = new LoginChecker(databaseManager);
        return INSTANCE;
    }

    @SneakyThrows
    public Status check(LoginPacket packet, String ip) {
        String[] clientData = packet.getClientData();
        String hwid = HwidCalculator.get().calculate(clientData);
        ClientEntity client = manager.getClientService().findById(hwid);
        if (client == null) return Status.CLIENT_NOT_FOUND;
        client.setLastIp(ip);

        String[] serviceData = packet.getServiceData();
        UUID serviceId = UUID.nameUUIDFromBytes(Utils.getBytes(serviceData));
        ResourceEntity resourceEntity = manager.getResourceService().findById(serviceId);
        if (resourceEntity == null) return Status.INVALID_SERVICE;

        LicenseService licenseService = manager.getLicenseService();
        UUID uuid = UUID.nameUUIDFromBytes(Utils.getBytes(clientData));
        LicenseEntity license = licenseService.findById(uuid);

        String licenseKey = packet.getLicenseKey();
        if (license == null && (license = licenseService.getByLicense(licenseKey)) == null) return Status.KEY_NOT_FOUND;
        if (!license.getKey().equals(licenseKey)) return Status.INVALID_KEY;
        if (license.hasExpired()) return Status.EXPIRED;

        if (!licenseService.isValidIp(license, ip)) {
            if (license.aboveMax()) return Status.MAX_IP;
            licenseService.addUsedIp(license, ip);
        }

        return Status.VALID;
    }

}
