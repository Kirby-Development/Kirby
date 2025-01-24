package dev.kirby.screenshare;

import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.config.ConfigManager;
import dev.kirby.packet.empty.ConnectPacket;
import dev.kirby.screenshare.config.Config;
import dev.kirby.screenshare.listener.PlayerListener;
import dev.kirby.screenshare.listener.ScreenShareEvents;
import dev.kirby.screenshare.netty.ScreenShareClient;
import dev.kirby.screenshare.packet.registry.Registry;
import dev.kirby.screenshare.player.ScreenShareManager;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class KirbySS extends KirbyPlugin {

    private final Map<Integer, Long> screenShareTime = new ConcurrentHashMap<>();

    private final ScreenShareManager manager;
    private final ScreenShareClient kirbySS = new ScreenShareClient(Registry.get(), this::shutdown, "KirbySS", manager());

    private final ConfigManager<Config> configManager;

    public KirbySS(Instance instance) {
        super(instance);
        configManager = new ConfigManager<>(instance.getDataFolder(), new Config());
        configManager.load();
        connect();
        manager = new ScreenShareManager(this);
        kirbySS.getEventRegistry().registerEvents(new ScreenShareEvents(this, manager, screenShareTime));
        kirbySS.setChannelActiveAction(ctx -> ctx.writeAndFlush(new ConnectPacket()));

        //todo papi time placeholder
        //todo papi util

    }

    @Override
    public void enable() {
        new PlayerListener(manager).register();
        kirbySS.connect("127.0.0.1", 6990);
    }

    @Override
    public void shutdown() {

    }
}
