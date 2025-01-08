package dev.kirby.database.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DatabaseTable(tableName = "License")
public class LicenseEntity extends DatabaseEntity<UUID> {

    @DatabaseField(id = true)
    private UUID id;

    @DatabaseField(canBeNull = false)
    private String key;

    @DatabaseField(canBeNull = false, foreign = true)
    private ResourceEntity service;

    @DatabaseField(canBeNull = false, foreign = true)
    private ClientEntity client;

    @DatabaseField(canBeNull = false)
    private long expireAt;

    @DatabaseField
    private int maxIpInUse;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<UsedIp> usedIps;

    public boolean hasExpiry() {
        return this.expireAt != 0L;
    }

    public @Nullable Instant getExpiry() {
        return hasExpiry() ? Instant.ofEpochSecond(this.expireAt) : null;
    }

    public boolean hasExpired() {
        Instant expiry = getExpiry();
        return expiry != null && expiry.isBefore(Instant.now());
    }


    public boolean aboveMax() {
        return usedIps.size() >= maxIpInUse;
    }
}