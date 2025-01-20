package dev.kirby.packet.registration;

public class LogoutPacket extends RegistrationPacket {

    public LogoutPacket(String[] clientData, String[] resourceData, String licenseKey) {
        super(clientData, resourceData, licenseKey);
    }

    public LogoutPacket() {
        super();
    }
}