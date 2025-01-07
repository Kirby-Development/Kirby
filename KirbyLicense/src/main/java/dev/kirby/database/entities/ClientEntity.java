package dev.kirby.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DatabaseTable(tableName = "Client")
public class ClientEntity extends DatabaseEntity<String> {

    @DatabaseField(id = true)
    private String hwid;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private LicenseEntity license;

    @DatabaseField
    private String lastIp;

    @Override
    public String getId() {
        return hwid;
    }

}