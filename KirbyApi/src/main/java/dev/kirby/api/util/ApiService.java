package dev.kirby.api.util;


import dev.kirby.api.KirbyApi;
import dev.kirby.service.ServiceHelper;
import dev.kirby.service.ServiceRegistry;

public interface ApiService extends ServiceHelper {

    @Override
    default ServiceRegistry manager(){
        return KirbyApi.getManager();
    }
}
