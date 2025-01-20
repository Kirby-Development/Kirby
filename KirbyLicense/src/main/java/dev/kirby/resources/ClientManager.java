package dev.kirby.resources;


import dev.kirby.ServerLauncher;
import dev.kirby.packet.registration.RegistrationPacket;
import dev.kirby.thread.ProfileThread;
import dev.kirby.thread.ThreadManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * todo stuff with ai
 */
public class ClientManager {

    private final Map<String, Connection> clients = new ConcurrentHashMap<>();
    private final ServerLauncher serverLauncher;
    private final int maxAttempts;
    private final ThreadManager threadManager;

    public ClientManager(ServerLauncher serverLauncher) {
        this.serverLauncher = serverLauncher;
        maxAttempts = serverLauncher.getConfig().getMaxAttempts();
        threadManager = serverLauncher.getThreadManager();
    }

    public void disconnect(RegistrationPacket packet) {
        Connection connection = get(packet);
        threadManager.shutdown(connection.getProfileThread());
        clients.remove(connection.getLicenseId());
    }

    public void connect(Connection connection, @NotNull String ip) {
        connection.setIp(ip);
        connection.reset();
        connection.valid();
        update(connection);
    }

    public Connection get(RegistrationPacket packet) {
        String licenseId = serverLauncher.getGenerator().generateSecureID(packet.getClientData(), packet.getResourceData(), new String[]{packet.getLicenseKey()});
        ProfileThread profileThread = threadManager.getAvailableProfileThread();
        return new Connection(licenseId, profileThread);
    }

    public boolean attempt(RegistrationPacket packet, @NotNull String ip) {
        String license = packet.getLicenseKey();
        if (!clients.containsKey(license)) {
            clients.put(license, get(packet));
            return true;
        }
        Connection connection = clients.get(license);
        connection.ip(ip);
        connection.setValid(false);
        return connection.getAttempts().get() < maxAttempts;
    }

    public void update(Connection connection) {
        clients.put(connection.getLicenseId(), connection);
    }
}
