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
import android.widget.Spinner;
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

    GithubAPI githubAPI;
    Spinner repositoriesSpinner;
    private Handler handler;

    private RepoItems repoList = new RepoItems();
    private RepoItems adapterData = new RepoItems();
    private RecyclerView recyclerView;
    private RepoAdapter mAdapter;
    ActionBar actionBar;
    private BottomNavigationView bottom_navigation;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new RepoAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        actionBar=getSupportActionBar();
        BottomNavigationView bottom_navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        createGithubAPI();

        mAdapter.setOnLoadMoreListener(new RepoAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapterData.setItems(null);
                mAdapter.notifyItemInserted(adapterData.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterData.remove(adapterData.size() - 1);
                        mAdapter.notifyItemRemoved(adapterData.size());
                        for (int i = 0; i < 15; i++) {
                            mAdapter.notifyItemInserted(adapterData.size());
                        }
                        mAdapter.setLoaded();
                    }
                }, 2000);
                System.out.println("load");
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void createGithubAPI() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GithubAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GithubAPI git = retrofit.create(GithubAPI.class);
        Call<RepoItems> call = (Call<RepoItems>) git.getRepos();
        call.enqueue(new Callback<RepoItems>() {
            @Override
            public void onResponse(Call<RepoItems> call, Response<RepoItems> response) {
                RepoItems model = response.body();
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
                    //200
                    mAdapter.setData(model);
                    adapterData = model;
                    Log.d("onresponse","responseBody Github Name : "+model.getItems().get(0).getName()
                            +"\nDescription : "+model.getItems().get(0).getDescription()
                            +"\nOwner : "+model.getItems().get(0).getOwner()
                            +"\nNum of stars : "+model.getItems().get(0).getNumberOfStars());

                }

            }

            @Override
            public void onFailure(Call<RepoItems> call, Throwable t) {
                Log.e("onfailure","on failure"+t,t);
            }
        });

    }


}
