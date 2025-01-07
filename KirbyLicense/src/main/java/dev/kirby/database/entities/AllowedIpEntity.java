package dev.kirby.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DatabaseTable(tableName = "AllowedIps")
public class AllowedIpEntity extends DatabaseEntity<Integer> {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private LicenseEntity license;

    @DatabaseField(canBeNull = false)
    private String ip;

    @Override
    public Integer getId() {
        return id;
    }
}