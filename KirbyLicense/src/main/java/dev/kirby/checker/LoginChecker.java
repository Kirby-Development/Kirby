package dev.kirby.checker;

import dev.kirby.ServerLauncher;
import dev.kirby.Utils;
import dev.kirby.checker.hwid.HwidCalculator;
import dev.kirby.checker.hwid.SecureUUIDGenerator;
import dev.kirby.database.entities.ClientEntity;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.entities.ResourceEntity;
import dev.kirby.database.services.LicenseService;
import dev.kirby.packet.LoginPacket;
import dev.kirby.packet.Status;
import lombok.SneakyThrows;

import java.util.UUID;

public class LoginChecker extends Checker<LoginPacket> {


    private final SecureUUIDGenerator uuidGenerator;
    private final ServerLauncher serverLauncher;
    private LoginChecker(ServerLauncher serverLauncher) {
        super(serverLauncher.getDatabaseManager());
        this.serverLauncher = serverLauncher;
        uuidGenerator = serverLauncher.getSecureUUIDGenerator();
    }

    private static LoginChecker INSTANCE;
    public static LoginChecker get(ServerLauncher serverLauncher) {
        if (INSTANCE == null) INSTANCE = new LoginChecker(serverLauncher);
        return INSTANCE;
    }

    @SneakyThrows
    public Status check(LoginPacket packet, String ip) {
        String[] clientData = packet.getClientData();
        String hwid = HwidCalculator.get(serverLauncher).calculate(clientData);
        ClientEntity client = manager.getClientService().findById(hwid);
        if (client == null) return Status.INVALID_USER;
        client.setLastIp(ip);

        String[] serviceData = packet.getServiceData();

        UUID serviceId = uuidGenerator.generateSecureUUID(serviceData);
        ResourceEntity resourceEntity = manager.getResourceService().findById(serviceId);
        if (resourceEntity == null) return Status.INVALID_SERVICE;

        LicenseService licenseService = manager.getLicenseService();

        String licenseKey = packet.getLicenseKey();
        String[] concat = Utils.concat(clientData, Utils.concat(serviceData, licenseKey));
        UUID uuid = uuidGenerator.generateSecureUUID(concat);
        LicenseEntity license = licenseService.findById(uuid);
        if (license == null) return Status.INVALID_KEY;

        if (license.hasExpired()) return Status.EXPIRED;

        if (!licenseService.isValidIp(license, ip)) {
            if (license.aboveMax()) return Status.MAX_IP;
            licenseService.addUsedIp(license, ip);
        }

        return Status.VALID;
    }

}
