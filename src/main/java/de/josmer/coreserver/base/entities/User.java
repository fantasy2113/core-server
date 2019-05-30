package de.josmer.coreserver.base.entities;

public final class User extends AbstractEntity {

    private String userEmail;
    private boolean isActive;
    private String pw;
    private String lastLogin;

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getPw() {
        return this.pw;
    }

    public void setPw(final String pw) {
        this.pw = pw;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
}
