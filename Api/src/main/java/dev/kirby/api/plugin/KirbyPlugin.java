package dev.kirby.api.plugin;

import dev.kirby.Initializer;
import dev.kirby.api.file.ConfigYaml;
import dev.kirby.api.netty.NettyClient;
import dev.kirby.KirbyLogger;
import dev.kirby.packet.registry.PacketRegister;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class KirbyPlugin implements Initializer {

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
    }

    public void init() {}
    public abstract void enable();
    public abstract void shutdown();

    public String[] data() {
        return new String[]{
                name,
                version
        };
    }
}
