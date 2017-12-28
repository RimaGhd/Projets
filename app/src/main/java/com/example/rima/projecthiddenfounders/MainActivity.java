package com.example.rima.projecthiddenfounders;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Handler handler;

    public RepoItems repoList = new RepoItems();
    private RecyclerView recyclerView;
    private RepoAdapter mAdapter;
    ActionBar actionBar;
    private int pageNumber = 1;
    private TextView tvEmptyView;
    private ProgressBar progressBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return false;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        handler = new Handler();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tvEmptyView = (TextView)findViewById(R.id.tvEmptyView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RepoAdapter(recyclerView,this);
        recyclerView.setAdapter(mAdapter);

        actionBar=getSupportActionBar();
        BottomNavigationView bottom_navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        createGithubAPI(pageNumber);
        /*if (repoList.getItems().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }*/
        mAdapter.setOnLoadMoreListener(new RepoAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d("logging","end scroll");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            pageNumber++;
                            createGithubAPI(pageNumber);
                        mAdapter.setLoaded();
                    }
                }, 1000);
                System.out.println("load");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void createGithubAPI(int pageNumber) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GithubAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        progressBar.setVisibility(View.VISIBLE);

        GithubAPI git = retrofit.create(GithubAPI.class);
        Call<RepoItems> call = (Call<RepoItems>) git.getRepos(pageNumber);
        call.enqueue(new Callback<RepoItems>() {
            @Override
            public void onResponse(Call<RepoItems> call, Response<RepoItems> response) {
                RepoItems model = response.body();
                progressBar.setVisibility(View.GONE);
                repoList = model;
                if (model==null) {
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody!=null) {
                        try {
                            Log.d("responseBody","responseBody = "+responseBody.string());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("responseBody","responseBody = null");
                    }
                } else {
                    mAdapter.setData(model);
                    //adapterData = model;
                    Log.d("onresponse","responseBody Github Name : "+model.getItems().get(0).getName()
                            +"\nDescription : "+model.getItems().get(0).getDescription()
                            +"\nOwner : "+model.getItems().get(0).getOwner().getLogin()
                            +"\nNum of stars : "+model.getItems().get(0).getNumberOfStars()
                            +"\nAvatar url : "+model.getItems().get(0).getOwner().getAvatarUrl());
                }

            }

            @Override
            public void onFailure(Call<RepoItems> call, Throwable t) {
                Log.e("onfailure","on failure"+t,t);
                progressBar.setVisibility(View.GONE);
            }
        });

    }


}
