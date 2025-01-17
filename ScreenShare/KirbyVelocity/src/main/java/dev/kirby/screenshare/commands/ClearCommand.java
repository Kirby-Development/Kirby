package dev.kirby.screenshare.commands;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.kirby.screenshare.PlayerState;
import dev.kirby.packet.registry.PacketSender;
import dev.kirby.screenshare.packet.StatePacket;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.SSPlayer;
import dev.kirby.screenshare.player.Session;
import dev.kirby.screenshare.utils.ServerUtils;
import net.kyori.adventure.text.Component;

public class ClearCommand extends SSCommand {

    private final Session.Manager sessionManager;
    private final PacketSender packetSender;

    public ClearCommand(Session.Manager sessionManager, SSManager manager, ProxyServer server) {
        super(server, manager);
        this.sessionManager = sessionManager;
        this.packetSender = get(PacketSender.class);
    }

    // /clear negro

    @Override
    protected void execute(Player p, String[] args) {
        Result result = result(p, args);
        if (result == null) return;
        final int sessionId = result.ssTarget().getSsId();
        if (!sessionManager.contains(sessionId)) {
            p.sendMessage(Component.text("not in a ss"));
            //todo not in a ss
            return;
        }

        final RegisteredServer ss = ServerUtils.getServer("hub");


        for (SSPlayer player : sessionManager.getSession(sessionId).getAll()) {
            player.setSsId(-1);
            Player p1 = player.getPlayer();
            packetSender.sendPacket(new StatePacket(player.getUuid(), PlayerState.NONE, -1));
            p1.createConnectionRequest(ss).fireAndForget();
        }

        sessionManager.delete(sessionId);

    }
}
