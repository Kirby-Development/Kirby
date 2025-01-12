package dev.kirby.screenshare.netty;

import dev.kirby.screenshare.KirbyVelocity;
import dev.kirby.screenshare.player.SSManager;

public record ServerEvents(KirbyVelocity kirbyVelocity, SSManager manager, dev.kirby.screenshare.player.Session.Manager sessions) {

}
