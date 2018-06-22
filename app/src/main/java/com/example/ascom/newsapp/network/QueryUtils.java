package com.example.ascom.newsapp.network;

import android.text.TextUtils;
import android.util.Log;

import com.example.ascom.newsapp.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private QueryUtils() {
    }
    public static List<News> fetchNewsStoriesData(String requestUrl) {

        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.fillInStackTrace();
        }

        // Extract relevant fields from the JSON response and create a list of {@link NewsItem}s.
        List<News> newsList = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link NewsItem}s.
        return newsList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.fillInStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Error", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Error", "Problem retrieving the news stories JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(newsJSON);

            JSONObject response = root.getJSONObject("response");

            JSONArray resultsArray = response.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject newsItem = resultsArray.getJSONObject(i);

                String title = newsItem.getString("webTitle");
                String section = newsItem.getString("sectionName");
                String date = newsItem.getString("webPublicationDate");
                String webUrl = newsItem.getString("webUrl");
                JSONArray tagsArray = newsItem.getJSONArray("tags");
                String author = "";
                int numberOfContributors = tagsArray.length();
                if (numberOfContributors > 0) {
                    for (int j = 0; j < numberOfContributors; j++) {
                        if (j > 0 && j < numberOfContributors - 1) {
                            author += ", ";
                        }
                        if (j > 0 && j == numberOfContributors - 1) {
                            author += " and ";
                        }
                        JSONObject tagItem = tagsArray.getJSONObject(j);
                        author += tagItem.getString("webTitle");
                    }
                }

                newsList.add(new News(title, section, author, date, webUrl));
            }

        } catch (JSONException e) {
            Log.e("Error", "Problem parsing the news stories JSON results", e);
        }

        return newsList;
    }
}
