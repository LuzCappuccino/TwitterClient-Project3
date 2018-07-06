package com.codepath.apps.restclienttemplate;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcel;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    // hook up and create and adapter
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweetList;
    RecyclerView rvTweets;
    static int rando = 3;
    // for swipe to refresh stuff
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        /* actionbar stuff */
        ActionBar bar = getActionBar();
        String color = "#4AA0EC";
//        bar.setBackgroundDrawable(new ColorDrawable(Color.RED));
        // find the recycler view
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // init the he array list (our data source)
        tweetList = new ArrayList<>();
        // construct the adapeter with the data source
        tweetAdapter= new TweetAdapter(tweetList);
        // recyclerview setup (layout manager, se adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        // set adapter
        rvTweets.setAdapter(tweetAdapter);

        client = TwitterApp.getRestClient(getApplicationContext());
        PopulateTimeline();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        /* set up stuff for our swipe container */
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /* gameplan: make a new network call to get an updated tweet list, clear the list,
                * and then populate the recycler view with this */
                fetchTimelineAsync(0);
                swipeContainer.setRefreshing(false);
            }

            private void fetchTimelineAsync(int i) {
                client.getHomeTimeline(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        // iterate through the JSON array, for each entry deserialize the object
                        tweetAdapter.clear();
                        for(int k = 0; k < response.length(); k++) {
                            Tweet tweet = null;
                            try {
                                tweet = Tweet.fromJSON(response.getJSONObject(k));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            tweetList.add(tweet);
                            tweetAdapter.notifyItemInserted(tweetList.size() - 1);
                        }
                        tweetAdapter.addAll(tweetList);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("TimelineActivity", "Fetch timeline error: " + throwable.toString());
                    }
                });
            }
        });
    }

    // creating an actiona bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose_bt, menu);
        return true;
    }

    // click listener for compose button
    public void onComposeAction(MenuItem mi) {
        // handle click here
        Intent compose = new Intent(getApplicationContext(), ComposeActivity.class);
        compose.putExtra("code", 10);
        compose.putExtra("bool", true);
        startActivityForResult(compose, rando);
    }

    // function for interpreting our result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(rando == requestCode && resultCode == RESULT_OK){
            Tweet sentTweet = Parcels.unwrap(data.getParcelableExtra(Tweet.class.getSimpleName()));
            Log.i("Timlineactivity", "we have recieved a tweet");
            tweetList.add(0, sentTweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
    }

    private void PopulateTimeline()
    {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Log.d("TwitterCLient", response.toString());
                // iterate through the JSON array, for each entry deserialize the object
                for(int i = 0; i < response.length(); i++){
                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tweetList.add(tweet);
                    tweetAdapter.notifyItemInserted(tweetList.size() - 1);
                }
                // convert each thing into a tweet model
                // add that tweet model to our data source
                // notify the adapter has changed

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Log.d("TwitterCLient", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterCLient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterCLient", errorResponse.toString());
                throwable.printStackTrace();            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterCLient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
