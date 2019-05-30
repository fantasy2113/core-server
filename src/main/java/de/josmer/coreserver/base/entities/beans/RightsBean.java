package de.josmer.coreserver.base.entities.beans;

import java.util.ArrayList;
import java.util.List;

public class RightsBean {
    private int userId;
    private String login;
    private List<RightBean> rights;

    public RightsBean() {
        this.rights = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<RightBean> getRights() {
        return rights;
    }

    public void setRights(List<RightBean> rights) {
        this.rights = rights;
    }
}
