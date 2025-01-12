package dev.kirby.screenshare.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kirby.screenshare.PlayerState;
import dev.kirby.screenshare.packet.PacketSender;
import dev.kirby.screenshare.packet.StatePacket;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.SSPlayer;
import dev.kirby.screenshare.player.Session;

public class ScreenShareCommand extends SSCommand {

    //todo perchè mappa e non lista? dio nero

    private final Session.Manager sessionManager;
    private final SSManager manager;
    private final ProxyServer server;
    private final PacketSender packetSender;

    public ScreenShareCommand(Session.Manager sessionManager, SSManager manager, ProxyServer server) {
        this.sessionManager = sessionManager;
        this.manager = manager;
        this.server = server;
        this.packetSender = get(PacketSender.class);
    }

    @Override
    protected void execute(Player p, String[] args) {
        SSPlayer player = manager.getProfile(p);
        if (player == null) return;
        if (sessionManager.contains(player.getSsId())) return;

        if (args.length == 0) {
            //todo not enouh args
            return;
        }



        packetSender.sendPacket(new StatePacket(player.getUuid(), PlayerState.STAFF, ));
    }
}
