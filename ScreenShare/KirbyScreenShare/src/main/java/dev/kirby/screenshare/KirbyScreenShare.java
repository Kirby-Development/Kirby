package dev.kirby.screenshare;

import dev.kirby.api.packet.PacketEvent;
import dev.kirby.api.plugin.KirbyPlugin;
import dev.kirby.packet.ConnectPacket;
import dev.kirby.screenshare.listener.PlayerListener;
import dev.kirby.screenshare.listener.ScreenShareEvents;
import dev.kirby.screenshare.packet.registry.Registry;
import dev.kirby.screenshare.player.ScreenShareManager;
import lombok.Getter;

@Getter
public class KirbyScreenShare extends KirbyPlugin implements PacketEvent {

    private final ScreenShareManager manager;
    private final ScreenShareClient kirbySS = new ScreenShareClient(Registry.get(), this::shutdown, "KirbySS", manager());

    public KirbyScreenShare(Instance instance) {
        super(instance);
        manager = new ScreenShareManager(this.instance);
        install(ScreenShareManager.class, manager);
        kirbySS.getEventRegistry().registerEvents(new ScreenShareEvents(this, manager));
        kirbySS.setChannelActiveAction(ctx -> ctx.writeAndFlush(new ConnectPacket()));
    }

    @Override
    public void enable() {
        new PlayerListener().register();
        kirbySS.connect("127.0.0.1", 6990);
    }

    @Override
    public void shutdown() {

    }

}
