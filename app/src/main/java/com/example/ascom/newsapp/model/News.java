package com.example.ascom.newsapp.model;

public class News {
    private String title;
    private String section;
    private String author;
    private String date;
    private String webUrl;

    public News(String title, String section, String author, String date, String webUrl) {
        this.title = title;
        this.section = section;
        this.author = author;
        this.date = date;
        this.webUrl = webUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
