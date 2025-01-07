package dev.kirby.checker.hwid;

import dev.kirby.checker.Checker;
import dev.kirby.database.DatabaseManager;
import dev.kirby.database.entities.AllowedIpEntity;
import dev.kirby.database.entities.ClientEntity;
import dev.kirby.database.entities.LicenseEntity;
import dev.kirby.packet.DataPacket;
import dev.kirby.packet.Status;

import java.util.List;

public class HwidChecker extends Checker<DataPacket> {

    private HwidChecker(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    private static HwidChecker INSTANCE;

    public static HwidChecker get(DatabaseManager databaseManager) {
        if (INSTANCE == null) INSTANCE = new HwidChecker(databaseManager);
        return INSTANCE;
    }

    public Status check(DataPacket packet, String ip) {
        String hwid = HwidCalculator.get().calculate(packet.getData());
        ClientEntity client = manager.getClientService().findById(hwid);
        if (client == null) return Status.NOT_FOUND;
        client.setLastIp(ip);
        LicenseEntity license = client.getLicense();
        if (license.hasExpired()) return Status.EXPIRED;
        List<AllowedIpEntity> allowedIps = manager.getIpService().get(ip, license);
        if (allowedIps.isEmpty()) return Status.INVALID_IP;
        return Status.VALID;
    }
}
