package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by luzcamacho on 7/6/18.
 */

@Parcel
public class entities {
    public ArrayList<String> media;

    public entities() {
        media=new ArrayList<>();
    };

    public static entities fromjson(JSONObject object) {
        entities field = new entities();
        JSONArray mediaArray = null;
        if(object.has("media")){
            try {
                mediaArray = object.getJSONArray("media");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            mediaArray = new JSONArray();
        }

        for(int i = 0; i < mediaArray.length(); i++){
            try {
                field.media.add(mediaArray.getJSONObject(i).getString("media_url_https"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return field;
    }
}
