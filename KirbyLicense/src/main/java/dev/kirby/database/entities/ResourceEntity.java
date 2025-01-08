package dev.kirby.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DatabaseTable(tableName = "service")
public class ResourceEntity extends DatabaseEntity<UUID> {
    @DatabaseField(id = true)
    private UUID id;

    @DatabaseField(canBeNull = false)
    private String name;


}
