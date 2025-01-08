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


}
