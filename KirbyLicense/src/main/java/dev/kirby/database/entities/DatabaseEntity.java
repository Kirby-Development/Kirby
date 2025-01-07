package dev.kirby.database.entities;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class DatabaseEntity<ID> {

    public abstract ID getId();

}
