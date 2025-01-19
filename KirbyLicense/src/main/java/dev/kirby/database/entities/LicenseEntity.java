package dev.kirby.database.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import dev.kirby.config.Config;
import dev.kirby.license.Edition;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "license")
public class LicenseEntity extends DatabaseEntity<String> {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private ResourceEntity service;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private ClientEntity client;

    @DatabaseField(canBeNull = false)
    private String edition;

    @DatabaseField(canBeNull = false)
    private long expireAt;

    @DatabaseField
    private int maxIpInUse;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<UsedIp> usedIps;

    public boolean hasExpiry() {
        return this.expireAt != 0L;
    }

    public Edition getEdition() {
        return Edition.valueOf(this.edition);
    }

    public Instant getExpiry() {
        return hasExpiry() ? Instant.ofEpochSecond(this.expireAt) : null;
    }

    public boolean hasExpired() {
        Instant expiry = getExpiry();
        return expiry != null && expiry.isBefore(Instant.now());
    }


    public boolean aboveMax() {
        return usedIps.size() >= maxIpInUse;
    }

    public LicenseEntity(String id, ResourceEntity service, ClientEntity client, Edition edition, Config config) {
        this.id = id;
        this.service = service;
        this.client = client;
        this.edition = edition.name();

        this.expireAt = config.getExpiryTime() == 0L ? 0L : Instant.now().getEpochSecond() + config.getExpiryTime();
        this.maxIpInUse = config.getMaxIps();

    }
}