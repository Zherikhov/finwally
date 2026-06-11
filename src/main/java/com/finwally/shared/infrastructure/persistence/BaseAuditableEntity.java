package com.finwally.shared.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseAuditableEntity extends BaseTimestampedEntity {

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;
}
