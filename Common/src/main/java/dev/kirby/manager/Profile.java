package dev.kirby.manager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Profile<Id> {

    private final Id id;

    public Profile(Id id) {
        this.id = id;
    }
}
