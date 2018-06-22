package com.example.ascom.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.LoaderManager;

import com.example.ascom.newsapp.adaptor.NewsAdapter;
import com.example.ascom.newsapp.model.News;
import com.example.ascom.newsapp.network.NewsLoader;

import java.util.ArrayList;
import java.util.List;

import static com.example.ascom.newsapp.constants.Links.GUARDIAN_URL;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<News>>{
    private NewsAdapter newsAdapter;
    private TextView emptyStateTextView;
    private ProgressBar loadingIndicator;
    private ListView  newsListView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         newsListView = findViewById(R.id.listview);
        emptyStateTextView = findViewById(R.id.empty_viewTxt);
        newsListView.setEmptyView(emptyStateTextView);
        loadingIndicator = findViewById(R.id.loading_indicatorView);
        newsAdapter = new NewsAdapter(MainActivity.this, new ArrayList<News>());
        newsListView.setAdapter(newsAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = (News) parent.getItemAtPosition(position);
                Uri uri = Uri.parse(news.getWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(1, null, this);
        }else {
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        NewsLoader newsLoader = new NewsLoader(this, GUARDIAN_URL);
        return newsLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        loadingIndicator.setVisibility(View.GONE);
        newsAdapter.clear();
        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
            Log.e("Load", "onLoadFinished()");
        } else {
            emptyStateTextView.setText(R.string.no_news_stories_found);
        }
        }



    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }
}
