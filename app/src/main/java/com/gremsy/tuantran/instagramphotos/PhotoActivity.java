package com.gremsy.tuantran.instagramphotos;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotoActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);


        photos = new ArrayList<>();

        // Create the adapter
        aPhotos = new InstagramPhotosAdapter(this, photos);
        // Find the list view
        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        // Set adapter
        lvPhotos.setAdapter(aPhotos);
        // Lookup the swipe container view
        fetchPopularPhotos();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                photos.clear();
                fetchPopularPhotos();

            }
        });


    }
    public void fetchPopularPhotos(){
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        // Trigger the get request
        client.get(url, null, new JsonHttpResponseHandler(){
            // onSuccess
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    // iterate array of posts
                    for(int i = 0; i<photosJSON.length(); i++){
                        // get the JSON object
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        // decode into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        // Author
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        // Profile image
                        photo.profile_image = photoJSON.getJSONObject("user").getString("profile_picture");
                        // Caption
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        // Url
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        // Image height
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        // Likes count
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        // Add object into the array
                        photos.add(photo);


                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

                //Callback
                aPhotos.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
            // onFailure

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

}
