package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    public EditText etMessage;
    public JsonHttpResponseHandler handler;
    public TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etMessage = (EditText) findViewById(R.id.etMessage);
        client = TwitterApp.getRestClient(this);

    }

    public void SubmitClick(View view) {
        Log.i("ComposeActivity", "Clicked registered");
        String Message = etMessage.getText().toString();
        Log.i("ComposeActivity", "Message: " + Message);
        client.sendTweet(Message, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet sendTweet = new Tweet();
                try {
                    Log.i("ComposeActivity", "before json from");

                    sendTweet = Tweet.fromJSON(response);
                    Log.i("ComposeActivity", "We in here");
                    // create the intent to send the new tweet back to parent activity
                    Intent data = new Intent();
                    data.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(sendTweet));
                    // send our result back to our parent class
                    setResult(RESULT_OK, data);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("ComposeActivity", "Failing");

            }
        });
    }
}
