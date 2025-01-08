package dev.kirby.api.netty;

import dev.kirby.api.util.KirbyLogger;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.packet.Status;
import dev.kirby.packet.TextPacket;
import lombok.Getter;
import org.apache.logging.log4j.Level;

@Getter
public class ClientEvents {

    private final NettyClient client;
    private final String[] data;
    private final String license;

    private final KirbyLogger logger = new KirbyLogger("KirbyLicense"){
        @Override
        public String log(Level level, Object... message) {
            String log = super.log(level, message);
            System.out.println(log);
            return log;
        }
    };

    public ClientEvents(NettyClient client, KirbyPlugin plugin) {
        this.client = client;
        this.data = plugin.data();
        this.license = plugin.getConfig().getLicense();
    }


    public ClientEvents(NettyClient client, String[] data, String license) {
        this.client = client;
        this.data = data;
        this.license = license;
    }

    @PacketSubscriber
    public void onPacketReceive(TextPacket packet) {
        logger.info("Received \"" + packet.getText() + "\"");
    }

    @PacketSubscriber
    public void onPacketReceive(Status.ResponsePacket packet) {
        Status status = packet.getStatus();
        boolean valid = status.valid();
        logger.log(valid ? Level.INFO : Level.WARN, "Received " + packet.getPacketName() + ": " + status);
        if (!valid)
            client.shutdown();

    }
}
