package dev.kirby.database.entities;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class DatabaseEntity<ID> {

    public abstract ID getId();

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DatabaseEntity<?> entity && entity.getId().equals(getId());
    }
}
