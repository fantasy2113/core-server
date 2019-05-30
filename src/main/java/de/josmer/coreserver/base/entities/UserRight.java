package de.josmer.coreserver.base.entities;

public final class UserRight extends AbstractEntity {

    private int userId;
    private String service;
    private String serviceRight;

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
    }

    public String getService() {
        return this.service;
    }

    public void setService(final String service) {
        this.service = service;
    }

    public String getServiceRight() {
        return this.serviceRight;
    }

    public void setServiceRight(final String serviceRight) {
        this.serviceRight = serviceRight;
    }

}
