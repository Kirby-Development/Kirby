package dev.kirby;

import io.netty.channel.ChannelHandlerContext;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.NetworkInterface;
import java.util.Collections;

@UtilityClass
public class Utils {

    public String[] getData() {
        return new String[]{
                System.getProperty("sun.arch.data.model"),
                String.valueOf(Runtime.getRuntime().availableProcessors()),
                System.getenv("PROCESSOR_IDENTIFIER"),
                System.getenv("PROCESSOR_ARCHITECTURE"),
                //System.getenv("PROCESSOR_ARCHITEW6432"),
                System.getenv("NUMBER_OF_PROCESSORS"),
                System.getenv("PROCESSOR_LEVEL"),
                System.getenv("PROCESSOR_REVISION"),
                getMAC()
        };
    }

    private String getMAC() {
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
    }

    public byte[] getBytes(String... data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        for (Object obj : data) objectStream.writeObject(obj);
        objectStream.flush();
        return byteStream.toByteArray();
    }

    public String getIp(ChannelHandlerContext ctx){
        return ctx.channel().remoteAddress().toString().substring(1);
    }

}
