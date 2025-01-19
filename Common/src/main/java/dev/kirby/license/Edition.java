package dev.kirby.license;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Edition {
    Customer(0),
    Premium(1),
    Enterprise(2);

    private final int value;
}
