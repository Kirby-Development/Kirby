package dev.kirby.checker;

import dev.kirby.ServerLauncher;
import dev.kirby.checker.hwid.SecureGenerator;
import dev.kirby.database.entities.ClientEntity;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.entities.ResourceEntity;
import dev.kirby.database.services.LicenseService;
import dev.kirby.packet.LoginPacket;
import dev.kirby.packet.Status;
import lombok.SneakyThrows;

public class LoginChecker extends Checker<LoginPacket> {

    public static boolean DEBUG = false;

    private final SecureGenerator generator;
    private final ServerLauncher serverLauncher;

    private LoginChecker(ServerLauncher serverLauncher) {
        super(serverLauncher.getDatabaseManager());
        this.serverLauncher = serverLauncher;
        generator = serverLauncher.getGenerator();
    }

    private static LoginChecker INSTANCE;

    public static LoginChecker get(ServerLauncher serverLauncher) {
        if (INSTANCE == null) INSTANCE = new LoginChecker(serverLauncher);
        return INSTANCE;
    }

    @SneakyThrows
    public Status check(LoginPacket packet, String ip) {
        ClientEntity client = manager.getClientService().findByData(serverLauncher, packet.getClientData());
        if (client == null) return Status.INVALID_USER;
        client.setLastIp(ip);

        ResourceEntity resourceEntity = manager.getResourceService().findByData(generator, packet.getServiceData());
        if (resourceEntity == null) return Status.INVALID_SERVICE;

        LicenseService licenseService = manager.getLicenseService();
        LicenseEntity license = licenseService.findByData(generator, packet);
        if (license == null) return Status.INVALID_KEY;
        if (license.hasExpired()) return Status.EXPIRED;
        if (!licenseService.isValidIp(license, ip)) {
            if (license.aboveMax()) return Status.MAX_IP;
            licenseService.addUsedIp(license, ip);
        }

        return Status.VALID;
    }
}
