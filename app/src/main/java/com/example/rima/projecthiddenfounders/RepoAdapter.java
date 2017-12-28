package com.example.rima.projecthiddenfounders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rima on 24/12/2017.
 */

public class RepoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GithubRepo> repoList;
    private OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private Activity activity;

    public RepoAdapter(RecyclerView recyclerView, Activity activity) {
        this.repoList = new ArrayList<>();
        this.activity = activity;
        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if(!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                        if(onLoadMoreListener != null){
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return repoList.get(position) != null ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.repo_list_row, parent, false);
            return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof RepoViewHolder){
            GithubRepo repositery = repoList.get(position);
            RepoViewHolder repoViewHolder = (RepoViewHolder) holder;
            repoViewHolder.repoName.setText(repositery.getName());
            repoViewHolder.repoDesc.setText(repositery.getDescription());
            repoViewHolder.repoOwner.setText(repositery.getOwner().getLogin());
            repoViewHolder.repoStars.setText(String.valueOf(repositery.getNumberOfStars()));
            Picasso.with(activity.getApplicationContext()).load(repositery.getOwner().getAvatarUrl()).into(repoViewHolder.avatarImage);
            //repoViewHolder.avatarImage.setImageURI(Uri.parse(repositery.getAvatarUrl()));
        }

    }

    public RepoAdapter() {
        this.repoList = new ArrayList<>();
    }

    public void setData(RepoItems list){
        this.repoList.addAll(list.getItems());
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return repoList.size();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder{

        TextView repoName, repoDesc, repoStars, repoOwner;
        ImageView avatarImage;
        public RepoViewHolder(View itemView) {
            super(itemView);
            repoName = (TextView)itemView.findViewById(R.id.repo_name);
            repoDesc = (TextView)itemView.findViewById(R.id.repo_description);
            repoStars = (TextView)itemView.findViewById(R.id.number_of_stars);
            repoOwner = (TextView)itemView.findViewById(R.id.repo_owner_name);
            avatarImage = (ImageView)itemView.findViewById(R.id.imageView);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }
    public interface OnLoadMoreListener {
        void onLoadMore();
    }
    public void setLoaded() {
        loading = false;
    }
}
