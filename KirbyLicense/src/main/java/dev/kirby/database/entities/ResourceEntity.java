package dev.kirby.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "service")
public class ResourceEntity extends DatabaseEntity<String> {
    @DatabaseField(id = true)
    private String id;

    @DatabaseField(canBeNull = false)
    private String name;


}
