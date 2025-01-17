package dev.kirby.api.plugin;

import dev.kirby.Initializer;
import dev.kirby.KirbyLogger;
import dev.kirby.api.file.ConfigYaml;
import dev.kirby.api.netty.NettyClient;
import dev.kirby.api.util.ApiService;
import dev.kirby.general.GeneralSender;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.io.Responder;
import dev.kirby.packet.ConnectPacket;
import dev.kirby.packet.registry.PacketRegister;
import dev.kirby.packet.registry.PacketSender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class KirbyPlugin implements Initializer, ApiService {

    private final NettyClient client;
    protected final KirbyInstance<? extends KirbyPlugin> instance;
    private final String name;
    private final String version;
    private final KirbyLogger logger;
    private final ConfigYaml config;

    public KirbyPlugin(KirbyInstance<? extends KirbyPlugin> kirby) {
        this.instance = kirby;
        this.name = this.instance.getName();
        this.version = this.instance.getPluginMeta().getVersion();
        this.config = new ConfigYaml(this.instance);
        this.logger = new KirbyLogger(this.name);
        client = new NettyClient(PacketRegister.get(), this, instance::shutdown);
        client.getEventRegistry().registerEvents(connectEvent);
        client.connect();
    }

    public void init() {
    }

    public abstract void enable();

    public abstract void shutdown();

    public String[] data() {
        return new String[]{
                name,
                version
        };
    }

    private final Object connectEvent = new Object() {

        @PacketSubscriber
        public void onConnect(final ConnectPacket packet, final Responder responder) {
            GeneralSender sender = client.getPacketSender();
            sender.setResponder(responder);
            install(Responder.class, responder);
            install(PacketSender.class, sender::sendPacket);
        }
    };

}
