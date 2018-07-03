package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.GlideApp;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

/**
 * Created by luzcamacho on 7/2/18.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    // pass in the tweets array
    private List<Tweet> myTweets;

    public TweetAdapter(List<Tweet> tweets){
        myTweets = tweets;
    }
    Context context;
    // for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // actually inflate tweet row
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        // only invoked when you have to create a new view, onBind will be called most often since
        // that is the method in charge of actually changing/modifying memory
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get data according to position
        Tweet tweet = myTweets.get(position);
        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        // TODO: put in profile image via glide
        GlideApp.with(context)
            .load(tweet.user.profileURL).into(holder.ivProfileImage);
    }

    // always necessary
    @Override
    public int getItemCount() {
        return myTweets.size();
    }

    // create viewholder class
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // declare all of my views
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
        }
    }
}
