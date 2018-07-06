package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ReplyActivity extends AppCompatActivity {

    private TwitterClient client;
    TextView tvrNewTweet;
    TextView charrCount;
    TextView rUsername;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        tvrNewTweet = (TextView) findViewById(R.id.tvrNewTweet);
        charrCount = (TextView) findViewById(R.id.charrCount);
        rUsername = (TextView) findViewById(R.id.rUsername);

        rUsername.setText(String.format("replying to @%s", tweet.user.name));

        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                charrCount.setText(String.valueOf(s.length()) + "/140");
            }

            public void afterTextChanged(Editable s) {
            }
        };

        tvrNewTweet.addTextChangedListener(mTextEditorWatcher);
    }

    public void ReplyRequest(View v) {
        client = TwitterApp.getRestClient(getApplicationContext());
        client.sendTweet(String.format("@%s %s", tweet.user.name, tvrNewTweet.getText().toString()), tweet.uid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    Tweet tweet1 = Tweet.fromJSON(response);
                    TimelineActivity.tweets.add(0, tweet1);
                    TimelineActivity.tweetAdapter.notifyItemInserted(0);
                    TimelineActivity.rvTweets.scrollToPosition(0);
                    //Intent i = new Intent(TweetDetailActivity.this, TimelineActivity.class);
                    Log.i("reply", String.format("@%s %s", tweet.user.name, tvrNewTweet.getText().toString()));
                    //startActivity(i);
                    finish();
                } catch (JSONException e) {
                    Log.e("retweet", "retweet failed");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("sendTweet", "sendtweet failed");
            }
        });


    }
}
