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
import dev.kirby.screenshare.player.ScreenSharePlayer;
import dev.kirby.utils.Utils;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class KirbySS extends KirbyPlugin {

    private final Map<Long, Long> screenShareTime = new ConcurrentHashMap<>();

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


        getPapi().add(player -> {
            ScreenSharePlayer profile = manager.getProfile(player);

            if (profile.getPlayerState() == PlayerState.NONE) return "";

            long id = profile.getSsId();
            if (id == -1) return "";

            Long l = screenShareTime.get(id);
            if (l == null) return "";

            return Utils.Time.format(l);
        }, "time");
    }

    @Override
    public void enable() {
        new PlayerListener(manager).register();

        kirbySS.connect("127.0.0.1", configManager.get().getPort());
    }

    @Override
    public void shutdown() {

    }
}
