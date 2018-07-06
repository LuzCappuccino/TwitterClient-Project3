package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    public EditText etMessage;
    public TextView tvCharCount;
    public JsonHttpResponseHandler handler;
    public TwitterClient client;
    public static String tag = "ComposeActivity";
    public boolean isCompose;
    public static Tweet repliedTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        // let's see what this is

        etMessage = (EditText) findViewById(R.id.etMessage);
        tvCharCount = (TextView) findViewById(R.id.tvCharCount);
        client = TwitterApp.getRestClient(this);

        /* listener for our text things */
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int msgLen = etMessage.getText().toString().length();
                int other = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int msgLen = etMessage.getText().toString().length();
                int other = charSequence.length();
                tvCharCount.setText(Integer.toString(other));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        isCompose = getIntent().getBooleanExtra("bool", false);

        if(isCompose != true){
            /* if we have to reply to a tweet */
            repliedTweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
            String repliedUser = repliedTweet.user.ScreenName;
            etMessage.setText(repliedUser);
        }
        else{
            /* if we simply have to compose a tweet */
            Toast.makeText(getApplicationContext(), "We are simply composing tweet", Toast.LENGTH_LONG).show();
        }
    }

    public void SubmitClick(View view) {


        String Message = etMessage.getText().toString();
        if(!isCompose){
            /* if we have received a command to reply to a tweet */
            client.replyTweet(repliedTweet.uid, Message, new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i(tag, "Reply was successful");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i(tag, "Reply was not successful :(");
                }
            });
        }
        else{
            /* if we are tasked with simply composing a tweet */
            client.sendTweet(Message, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Tweet sendTweet = new Tweet();
                    try {
                        sendTweet = Tweet.fromJSON(response);
                        Intent data = new Intent();
                        data.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(sendTweet));
                        setResult(RESULT_OK, data);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }


//        String Message = etMessage.getText().toString();
//        client.sendTweet(Message, new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Tweet sendTweet = new Tweet();
//
//
//
//                try {
//                    Log.i("ComposeActivity", "before json from");
//
//                    sendTweet = Tweet.fromJSON(response);
//                    Log.i("ComposeActivity", "We in here");
//                    // create the intent to send the new tweet back to parent activity
//                    Intent data = new Intent();
//                    data.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(sendTweet));
//                    // send our result back to our parent class
//                    setResult(RESULT_OK, data);
//                    finish();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                Log.i("ComposeActivity", "Failing");
//            }
//        });
    }

}
