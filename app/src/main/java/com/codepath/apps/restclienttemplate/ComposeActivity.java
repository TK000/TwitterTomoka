package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

//@Parcelable
public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    TextView tvNewTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        tvNewTweet = (TextView) findViewById(R.id.tvNewTweet);
    }

    public final int REQUEST_CODE = 20;

    public void networkRequest(View v) {
        client = TwitterApp.getRestClient(getApplicationContext());
        client.sendTweet(tvNewTweet.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // ActivityOne.java
                // REQUEST_CODE can be any value we like, used to determine the result type later
                // FirstActivity, launching an activity for a result
                Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
                //i.putExtra("mode", 2); // pass arbitrary data to launched activity
                startActivityForResult(i, REQUEST_CODE);
                //setResult(RESULT_OK); // set result code and bundle data for response
                //finish(); // closes the activity, pass data to parent
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("sendTweet", "sendtweet failed");
            }
        });
    }


}
