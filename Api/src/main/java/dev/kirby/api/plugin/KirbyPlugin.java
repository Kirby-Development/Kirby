package dev.kirby.api.plugin;

import dev.kirby.api.KirbyApi;
import dev.kirby.api.file.ConfigYaml;
import dev.kirby.api.netty.NettyClient;
import dev.kirby.api.util.KirbyLogger;
import dev.kirby.packet.registry.PacketRegister;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class KirbyPlugin {

    private final NettyClient client;
    protected final KirbyInstance<?> instance;
    private final String name;
    private final String version;
    private final KirbyLogger logger;
    private final ConfigYaml config;

    public KirbyPlugin(KirbyInstance<?> kirby) {
        this.instance = kirby;
        this.name = this.instance.getName();
        this.version = this.instance.getPluginMeta().getVersion();
        this.config = new ConfigYaml(this.instance);
        this.logger = new KirbyLogger(this.name);
        KirbyApi.getRegistry().install(this);
        client = new NettyClient(PacketRegister.get(), this, instance::shutdown);
    }

    protected void init() {
    }

    protected void enable() {
    }

    protected void shutdown() {
    }

    public String[] data() {
        return new String[]{
                name,
                version
        };
    }
}
