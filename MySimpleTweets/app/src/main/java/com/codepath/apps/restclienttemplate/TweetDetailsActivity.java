package com.codepath.apps.restclienttemplate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.GlideApp;
import com.codepath.apps.restclienttemplate.models.ParseRelativeDate;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    public TextView tvDetailsBody;
    public TextView tvDetailsName;
    public TextView tvDetailsUsername;
    public TextView tvDetailsTime;
    public ImageView ivDetailsProfilePic;
    // our unwrapped tweet
    Tweet viewTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tvDetailsBody = (TextView) findViewById(R.id.tvDetailsBody);
        tvDetailsName = (TextView) findViewById(R.id.tvDetailsName);
        tvDetailsUsername = (TextView) findViewById(R.id.tvDetailsUsername);
        tvDetailsTime = (TextView) findViewById(R.id.tvDetailsTime);
        ivDetailsProfilePic = (ImageView) findViewById(R.id.ivDetailsProfilePic);

        // unwrap our tweet from prev activity
        viewTweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        // set all our stuff equal to that shit
        tvDetailsBody.setText(viewTweet.body);
        tvDetailsName.setText(viewTweet.user.name);
        tvDetailsUsername.setText(viewTweet.user.ScreenName);
        tvDetailsTime.setText(ParseRelativeDate.getRelativeTimeAgo(viewTweet.createdAt));
        // use glide to post the profile app
        GlideApp.with(getApplicationContext())
                .load(viewTweet.user.profileURL)
                .into(ivDetailsProfilePic);

    }
}
