package dev.kirby.screenshare;

import com.github.retrooper.packetevents.PacketEvents;
import dev.kirby.api.plugin.KirbyInstance;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public final class Instance extends KirbyInstance<KirbySS>  {

    private final KirbySS kirbySS = new KirbySS(this);

    @Override
    public void onLoad() {
        super.onLoad();
        //todo fix
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        PacketEvents.getAPI().terminate();
    }

    @Override
    protected KirbySS load() {
        return kirbySS;
    }

}
