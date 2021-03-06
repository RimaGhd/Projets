package com.example.rima.projecthiddenfounders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rima on 24/12/2017.
 */

public class GithubRepo {

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("owner")
    @Expose
    RepoOwner owner;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("watchers_count")
    @Expose
    int numberOfStars;

    public GithubRepo(String name, RepoOwner owner, String description, int numberOfStars) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.numberOfStars = numberOfStars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RepoOwner getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }


}
