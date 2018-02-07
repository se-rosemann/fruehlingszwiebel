package de.god.fruehlingszwiebeldemo.api;

import java.util.UUID;

public class EntityReadModel {
    private UUID id = null;

    private Long version = null;

    public UUID getId() {
        return id;
    }

    public void setId(UUID pId) {
        id = pId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long pVersion) {
        version = pVersion;
    }
}
