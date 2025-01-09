package dev.kirby.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "client")
public class ClientEntity extends DatabaseEntity<String> {

    @DatabaseField(id = true, unique = true)
    private String id;

    @DatabaseField
    private String lastIp;

}