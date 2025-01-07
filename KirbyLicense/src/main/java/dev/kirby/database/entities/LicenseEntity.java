package dev.kirby.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DatabaseTable(tableName = "License")
public class LicenseEntity extends DatabaseEntity<UUID> {

    @DatabaseField(id = true)
    private UUID uuid;

    @DatabaseField(canBeNull = false)
    private String key;

    @DatabaseField(canBeNull = false)
    private long expireAt;

    @Override
    public UUID getId() {
        return uuid;
    }

    public boolean hasExpiry() {
        return this.expireAt != 0L;
    }

    public @Nullable Instant getExpiry() {
        return hasExpiry() ? Instant.ofEpochSecond(this.expireAt) : null;
    }

    public boolean timeValid() {
        return !hasExpiry();
    }

    public boolean hasExpired() {
        Instant expiry = getExpiry();
        return expiry != null && expiry.isBefore(Instant.now());
    }


    public @Nullable Duration getExpiryDuration() {
        Instant expiry = getExpiry();
        if (expiry == null) return null;
        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        return Duration.between(now, expiry);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LicenseEntity license)) return false;
        return getExpireAt() == license.getExpireAt() && Objects.equals(getUuid(), license.getUuid()) && Objects.equals(getKey(), license.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getKey(), getExpireAt());
    }
}