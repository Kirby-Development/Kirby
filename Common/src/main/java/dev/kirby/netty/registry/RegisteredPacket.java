package dev.kirby.netty.registry;

import dev.kirby.netty.Packet;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

@Getter
public class RegisteredPacket {

    private final Class<? extends Packet> packetClass;
    private final Constructor<? extends Packet> constructor;

    public RegisteredPacket(Class<? extends Packet> packetClass) throws NoSuchMethodException {
        this.packetClass = packetClass;

        List<Constructor<?>> emptyConstructorList = Arrays.stream(packetClass.getConstructors())
                .filter(constructor -> constructor.getParameterCount() == 0).toList();
        if (emptyConstructorList.isEmpty())
            throw new NoSuchMethodException("Packet " + packetClass.getSimpleName() + " is missing no-args-constructor");

        this.constructor = (Constructor<? extends Packet>) emptyConstructorList.get(0);
    }

}
