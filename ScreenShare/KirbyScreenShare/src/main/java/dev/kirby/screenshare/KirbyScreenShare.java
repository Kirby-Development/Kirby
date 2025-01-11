package dev.kirby.screenshare;

import dev.kirby.api.packet.PacketEvent;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.api.util.ServiceHelper;
import dev.kirby.screenshare.listener.PlayerListener;
import dev.kirby.screenshare.player.ScreenShareManager;
import lombok.Getter;

@Getter
public class KirbyScreenShare extends KirbyPlugin implements PacketEvent, ServiceHelper  {

    private final ScreenShareManager manager;
    private final ScreenShareClient client = new ScreenShareClient(Registry.get(), );

    public KirbyScreenShare(Instance instance) {
        super(instance);
        manager = new ScreenShareManager(this.instance);
        install(ScreenShareManager.class, manager);
        client.eventRegistry.registerEvents(new ScreenShareEvents(this))
    }

    @Override
    public void enable() {
        new PlayerListener().register();
        client.connect("127.0.0.1": 6990);
    }

    @Override
    public void shutdown() {

    }
}
