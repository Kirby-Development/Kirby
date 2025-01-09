package dev.kirby.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "ips")
public class UsedIp extends DatabaseEntity<Integer> {
    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private String ip;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private LicenseEntity license;

    public UsedIp(String ip, LicenseEntity license) {
        this.ip = ip;
        this.license = license;
    }
}
