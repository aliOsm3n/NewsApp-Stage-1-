package com.example.ascom.newsapp.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

import com.example.ascom.newsapp.model.News;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (url == null) {
            return null;
        }
        return QueryUtils.fetchNewsStoriesData(url);
    }
}
