package dev.kirby.config;

import dev.kirby.license.Edition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@ConfigInfo(format = Format.JSON)
public class Datas {

    private final Set<Client> clients = new HashSet<>();
    private final Set<Resource> resources = new HashSet<>();
    private final Set<License> licenses = new HashSet<>();

    @Data
    @AllArgsConstructor
    public static class Client {
        long id;
        String[] data;
        String ip;
    }

    @Data
    @AllArgsConstructor
    public static class Resource {
        long id;
        String name;
        String[] data;
    }

    @Data
    @AllArgsConstructor
    public static class License {
        long clientId;
        long resourceId;
        String license;
        Edition edition;


        public Client getClient(Datas datas) {
            return datas.getClients().stream()
                    .filter(client -> client.getId() == clientId)
                    .findFirst()
                    .orElse(null);
        }

        public Resource getResource(Datas datas) {
            return datas.getResources().stream()
                    .filter(resource -> resource.getId() == resourceId)
                    .findFirst()
                    .orElse(null);
        }
    }

}
