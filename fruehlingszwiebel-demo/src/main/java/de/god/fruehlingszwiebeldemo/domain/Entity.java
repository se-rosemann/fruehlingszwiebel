package de.god.fruehlingszwiebeldemo.domain;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.UUID;

/**
 * Base-Class for an entity, that is (global) identifiable by an id and its class.
 */
@MappedSuperclass
public abstract class Entity {
    @Id
    private UUID id = UUID.randomUUID();

    @Version
    private Long version = null;

    public UUID getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }
}
