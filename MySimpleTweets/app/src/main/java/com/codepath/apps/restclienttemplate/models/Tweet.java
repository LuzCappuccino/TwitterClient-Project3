package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by luzcamacho on 7/2/18.
 */

@Parcel
public class Tweet {
    // list out all tweet attributes
    public String body;
    public long uid;
    public String createdAt;
    public User user;
    public Boolean fav;
    public Boolean retweet;
    public entities fields;

    public Tweet() {
    }

    // deserialize the data
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        //extract all the values from the jSON
        tweet.body = jsonObject.getString("text");
        tweet.fav = jsonObject.getBoolean("favorited");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.retweet = jsonObject.getBoolean("retweeted");
        tweet.fields = entities.fromjson(jsonObject.getJSONObject("entities"));

        return tweet;
    }

}
