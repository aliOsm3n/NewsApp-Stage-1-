package com.example.ascom.newsapp.adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ascom.newsapp.R;
import com.example.ascom.newsapp.model.News;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    Context context ;
    ArrayList<News> newsArrayList ;


 public  NewsAdapter (Context context , ArrayList<News> newsArrayList){
     super(context , 0 , newsArrayList);
     this.context =context;
    this.newsArrayList = newsArrayList;
}


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
     convertView = LayoutInflater.from(context).inflate(R.layout.list_item , parent , false);
     News news = getItem(position);
        TextView titleTextView = convertView.findViewById(R.id.main_title);
        TextView sectionTextView = convertView.findViewById(R.id.main_section);
        TextView authorTextView = convertView.findViewById(R.id.main_author);
        TextView dateTextView = convertView.findViewById(R.id.main_date);
        titleTextView.setText(news.getTitle());
        sectionTextView.setText(news.getSection());
        authorTextView.setText(news.getAuthor());
        dateTextView.setText(getDateOnly(news.getDate()));
        return convertView;
    }

    private String getDateOnly(String date) {
        String[] parts = date.split("T");
        return parts[0];
    }
}
