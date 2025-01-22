package dev.kirby;

import dev.kirby.config.ConfigManager;
import dev.kirby.config.License;
import dev.kirby.license.NettyClient;
import dev.kirby.packet.registry.PacketRegister;
import dev.kirby.service.ServiceHelper;
import dev.kirby.service.ServiceRegistry;
import dev.kirby.utils.Destroyable;
import dev.kirby.exception.InvalidException;
import lombok.Getter;

@Getter
public abstract class KirbyResource implements ServiceHelper, Destroyable {

    protected final String name;
    protected final String version;
    protected final NettyClient licenseClient;

    protected KirbyResource(String name, String version) {
        this.name = name;
        this.version = version;
        licenseClient = new NettyClient(PacketRegister.get(), this, this::destroy, manager());
    }

    @Override
    public abstract ServiceRegistry manager();

    public String[] data() {
        return new String[]{name, version};
    }

    public abstract <T extends License> ConfigManager<T> getConfigManager();

    public <T extends License> void setLicense( String license) {
        ConfigManager<License> manager = getConfigManager();
        manager.get().setLicense(license);
        manager.save();
    }

    public <T extends License> String getLicense() {
        String license = getConfigManager().get().getLicense();
        if (license != null)
            return license;
        setLicense("INSERT-LICENSE-HERE");
        throw new InvalidException(InvalidException.Type.LICENSE);
    }

    public abstract void destroy();

}
