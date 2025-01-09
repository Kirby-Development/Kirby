package dev.kirby.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {

    private int maxIps = 5;
    private long expiryTime = 0L;
    private String securityKey = "LUCKY";

    private Database database = new Database();


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Database {

        private Mode mode = Mode.SQLite;

        private String database = "kirby";
        private String host = "localhost";
        private String port = "3306";

        private String username = "root";
        private String password = "";

        public enum Mode{
            SQLite,
            MySQL;
        }
    }

}
