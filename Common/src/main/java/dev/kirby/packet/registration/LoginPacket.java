package dev.kirby.packet.registration;

public class LoginPacket extends RegistrationPacket {

    public LoginPacket(String[] clientData, String[] resourceData, String licenseKey) {
        super(clientData, resourceData, licenseKey);
    }

    public LoginPacket() {
        super();
    }
}