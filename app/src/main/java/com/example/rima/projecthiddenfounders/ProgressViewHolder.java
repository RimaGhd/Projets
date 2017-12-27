package com.example.rima.projecthiddenfounders;

import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by rima on 26/12/2017.
 */

public class ProgressViewHolder extends RepoAdapter.RepoViewHolder {
    public ProgressBar progressBar;

    public ProgressViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);

    }
}
