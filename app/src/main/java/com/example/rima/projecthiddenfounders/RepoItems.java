package com.example.rima.projecthiddenfounders;

import java.util.List;

/**
 * Created by rima on 25/12/2017.
 */

public class RepoItems {

    private List<GithubRepo> items ;

    public List<GithubRepo> getItems() {
        return items;
    }

    public void setItems(List<GithubRepo> items) {
        this.items = items;
    }

    public int size(){
        return items.size();
    }

    public void remove(int i) {
        items.remove(i);
    }
}
