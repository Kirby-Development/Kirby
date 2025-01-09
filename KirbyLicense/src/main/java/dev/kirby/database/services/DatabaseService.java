package dev.kirby.database.services;

import com.j256.ormlite.dao.Dao;
import dev.kirby.database.entities.DatabaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.SQLException;

@Getter
@AllArgsConstructor
public abstract class DatabaseService<Entity extends DatabaseEntity<ID>, ID> {

    protected final Dao<Entity, ID> dao;

    @SneakyThrows
    public Entity create(final Entity entity) {
        dao.createIfNotExists(entity);
        return entity;
    }

    @SneakyThrows
    public boolean exits(final ID id) {
        return dao.idExists(id);
    }


    public Entity findById(final ID id) throws SQLException {
        return dao.queryForId(id);
    }

    public Entity update(final Entity stats) {
        new Thread(() -> r(stats)).start();
        return stats;
    }

    @SneakyThrows
    private void r(final Entity stats) {
        dao.createOrUpdate(stats);
    }
}
