package com.example.rima.projecthiddenfounders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepoOwner {

    private String login;

    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public String getLogin() {
        return login;
    }

}
