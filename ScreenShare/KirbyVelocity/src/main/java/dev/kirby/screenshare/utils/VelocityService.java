package dev.kirby.screenshare.utils;

import dev.kirby.screenshare.KirbyVelocity;
import dev.kirby.service.ServiceHelper;
import dev.kirby.service.ServiceRegistry;

public interface VelocityService extends ServiceHelper {

    @Override
    default ServiceRegistry manager() {
        return KirbyVelocity.MANAGER;
    }
}
