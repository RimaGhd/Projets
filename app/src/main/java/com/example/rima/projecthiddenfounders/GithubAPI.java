package com.example.rima.projecthiddenfounders;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by rima on 24/12/2017.
 */

public interface GithubAPI {

    String ENDPOINT = "https://api.github.com/";
    @GET("search/repositories?q=created:>2017-10-22&sort=stars&order=desc&page=1")
    Call<RepoItems> getRepos();

    }
