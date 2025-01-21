package dev.kirby;

import dev.kirby.license.NettyClient;
import dev.kirby.packet.registry.PacketRegister;
import dev.kirby.service.ServiceHelper;
import dev.kirby.service.ServiceRegistry;
import dev.kirby.utils.Destroyable;
import lombok.Getter;

@Getter
public abstract class KirbyResource implements ServiceHelper, Destroyable {

    protected final String name;
    protected final String version;
    protected final NettyClient client;

    protected KirbyResource(String name, String version) {
        this.name = name;
        this.version = version;
        client = new NettyClient(PacketRegister.get(), this, this::destroy, manager());
    }

    @Override
    public abstract ServiceRegistry manager();

    public String[] data() {
        return new String[]{name, version};
    }

    public abstract String getLicense();

    public abstract void destroy();
}
