package com.example.rima.projecthiddenfounders;

/**
 * Created by rima on 25/12/2017.
 */

public class RepoOwner {

    private String name;

    public RepoOwner() {
    }

    public RepoOwner(String name, String login) {

        this.name = name;
        this.login = login;
    }

    public String getLogin() {

        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String login;
}
