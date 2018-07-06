package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.GlideApp;
import com.codepath.apps.restclienttemplate.models.ParseRelativeDate;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by luzcamacho on 7/2/18.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    // pass in the tweets array
    private static List<Tweet> myTweets;

    public TweetAdapter(List<Tweet> tweets){
        myTweets = tweets;
    }
    static Context context;
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
        holder.tvTime.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.createdAt));
        holder.tvScreenName.setText(tweet.user.ScreenName);
        GlideApp.with(context)
            .load(tweet.user.profileURL).into(holder.ivProfileImage);
    }

    // always necessary
    @Override
    public int getItemCount() {
        return myTweets.size();
    }

    /* methods for clearing and adding a new data set */
    public void clear() {
        myTweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> newTweets) {
        myTweets.addAll(newTweets);
        notifyDataSetChanged();
    }

    // create viewholder class
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // declare all of my views
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvScreenName;
        public TextView tvTime;
        public ImageButton ibReply;
        public ImageButton ibLike;
        public ImageButton ibRetweet;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            ibReply = (ImageButton) itemView.findViewById(R.id.ibReply);

            ibReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isCompose = false;
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        Tweet tweet = myTweets.get(position);
                        Intent intent = new Intent(context, ComposeActivity.class);
                        intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                        intent.putExtra("bool", isCompose);

                        ( (Activity)context).startActivityForResult(intent, 3);
                    }
                }
            });
            /*need to call for it to actually work*/
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            /* do the things */
            if(position != RecyclerView.NO_POSITION){
                // get the tweet we want
                Tweet tweet = myTweets.get(position);
                // initialize the new intent
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // start out activity with created intent
                context.startActivity(intent);
            }
        }
    }
}
