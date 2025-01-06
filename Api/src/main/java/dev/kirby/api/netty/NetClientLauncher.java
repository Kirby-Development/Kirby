/*
 * Copyright (c) 2021, Pierre Maurice Schwang <mail@pschwang.eu> - MIT
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package dev.kirby.api.netty;

import dev.kirby.netty.event.EventRegistry;
import dev.kirby.netty.event.PacketSubscriber;
import dev.kirby.netty.exception.PacketRegistrationException;
import dev.kirby.netty.io.Responder;
import dev.kirby.netty.registry.IPacketRegistry;
import dev.kirby.netty.registry.SimplePacketRegistry;
import dev.kirby.packet.ConnectPacket;
import dev.kirby.packet.DataPacket;
import dev.kirby.packet.Status;
import dev.kirby.packet.TextPacket;
import io.netty.channel.ChannelHandlerContext;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.function.Supplier;

public class NetClientLauncher {

    public static void main(String[] args) throws PacketRegistrationException {
        IPacketRegistry packetRegistry = new SimplePacketRegistry();
        packetRegistry.registerPacket(0, ConnectPacket.class);
        packetRegistry.registerPacket(1, DataPacket.class);
        packetRegistry.registerPacket(2, Status.Packet.class);
        packetRegistry.registerPacket(3, TextPacket.class);

        Object[] data = {
                System.getProperty("sun.arch.data.model"),
                Runtime.getRuntime().availableProcessors(),
                System.getenv("PROCESSOR_IDENTIFIER"),
                System.getenv("PROCESSOR_ARCHITECTURE"),
                System.getenv("PROCESSOR_ARCHITEW6432"),
                System.getenv("NUMBER_OF_PROCESSORS"),
                System.getenv("PROCESSOR_LEVEL"),
                System.getenv("PROCESSOR_REVISION"),
                ((Supplier<String>) () -> {
                    try {
                        for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                            byte[] mac = networkInterface.getHardwareAddress();
                            if (mac != null) {
                                StringBuilder sb = new StringBuilder();
                                for (byte b : mac) sb.append(String.format("%02X", b));
                                return sb.toString();
                            }
                        }
                    } catch (Exception ignored) {
                    }
                    return "UNKNOWN";
                }).get()
        };

        EventRegistry eventRegistry = new EventRegistry();
        NettyClient client = new NettyClient(packetRegistry, future -> System.out.println("Client running"), eventRegistry);

        eventRegistry.registerEvents(new Object() {
            @PacketSubscriber
            public void onPacketReceive(ConnectPacket packet, ChannelHandlerContext ctx, Responder responder) {
                System.out.println("Received " + packet.getPacketName() + " from " + ctx.channel().remoteAddress().toString());
                responder.respond(new DataPacket(data));
                responder.respond(new TextPacket("license"));
            }

            @PacketSubscriber
            public void onPacketReceive(TextPacket packet, ChannelHandlerContext ctx) {
                System.out.println("Received " + packet.getText() + " from " + ctx.channel().remoteAddress().toString());
            }

            @PacketSubscriber
            public void onPacketReceive(Status.Packet packet, ChannelHandlerContext ctx) {
                Status status = packet.getStatus();
                System.out.println("Received " + status + " from " + ctx.channel().remoteAddress().toString());

                if (!status.valid()) {
                    System.out.println("shutdown");
                }
            }
        });

    }

}
