package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by luzcamacho on 7/2/18.
 */

@Parcel
public class User {
    // list all the attributes and deserialize JSON again
    public String name; //name
    public long uid;
    public String ScreenName; // the at??
    public String profileURL;

    public User() {
    }

    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();
        // extract and fill out values
        user.name = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.ScreenName = jsonObject.getString("screen_name");
        user.profileURL = jsonObject.getString("profile_image_url");
        user.ScreenName = "@" + user.ScreenName;
        return user;
    }
}
