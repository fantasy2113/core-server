package de.josmer.coreserver.base.interfaces.connector;

public class ConnectorResponse {

    private int updates;
    private int entityId;

    public int getUpdates() {
        return updates;
    }

    public void setUpdates(int updates) {
        this.updates = updates;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

}
