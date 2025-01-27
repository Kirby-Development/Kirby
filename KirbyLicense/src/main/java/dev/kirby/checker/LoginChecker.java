package dev.kirby.checker;

import dev.kirby.ServerLauncher;
import dev.kirby.checker.hwid.SecureGenerator;
import dev.kirby.database.entities.ClientEntity;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.database.entities.ResourceEntity;
import dev.kirby.database.services.LicenseService;
import dev.kirby.packet.registration.LoginPacket;
import dev.kirby.packet.registration.Status;
import lombok.SneakyThrows;

public class LoginChecker extends Checker<LoginPacket> {

    public static final boolean DEBUG = false, BYPASS = false;

    private final SecureGenerator generator;
    private final ServerLauncher serverLauncher;
    private final Checker.Debug debug;
    private LoginChecker(ServerLauncher serverLauncher) {
        super(serverLauncher.getDatabaseManager());
        this.serverLauncher = serverLauncher;
        generator = serverLauncher.getGenerator();
        debug = new Debug(manager);
    }

    private static LoginChecker INSTANCE;

    public static LoginChecker get(ServerLauncher serverLauncher) {
        if (INSTANCE == null) INSTANCE = new LoginChecker(serverLauncher);
        return INSTANCE;
    }

    @SneakyThrows
    public Status check(LoginPacket packet, String ip) {
        if (DEBUG) System.out.println("Checking client");
        if (BYPASS) return debug.check(packet, ip);
        ClientEntity client = manager.getClientService().findByData(serverLauncher, packet.getClientData());
        if (client == null) return Status.INVALID_USER;
        client.setLastIp(ip);

        if (DEBUG) System.out.println("Checking resource");
        ResourceEntity resourceEntity = manager.getResourceService().findByData(generator, packet.getResourceData());
        if (resourceEntity == null) return Status.INVALID_RESOURCE;

        if (DEBUG) System.out.println("Checking license");
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