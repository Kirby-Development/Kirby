package dev.kirby.database.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import dev.kirby.config.Config;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DatabaseTable(tableName = "license")
public class LicenseEntity extends DatabaseEntity<UUID> {

    @DatabaseField(id = true)
    private UUID id;

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

    public LicenseEntity(UUID id, ResourceEntity service, ClientEntity client, Config config) {
        this.id = id;
        this.service = service;
        this.client = client;

        this.expireAt = config.getExpiryTime() == 0L ? 0L : Instant.now().getEpochSecond() + config.getExpiryTime();
        this.maxIpInUse = config.getMaxIps();

    }
}