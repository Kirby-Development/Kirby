package dev.kirby.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ConfigInfo(name = "config")
public class License {

    private String license = "INSERT-LICENSE-HERE";


}
