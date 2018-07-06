package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;


public class TweetDetailActivity extends AppCompatActivity {

    Tweet tweet;
    TextView tvdUser;
    TextView tvdBody;
    TextView tvdCreatedAt;
    ImageView ivdProfile;
    ImageView ivMedia;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        tvdUser = (TextView) findViewById(R.id.tvdUser);
        tvdBody = (TextView) findViewById(R.id.tvdBody);
        tvdCreatedAt = (TextView) findViewById(R.id.tvdCreatedAt);
        ivdProfile = (ImageView) findViewById(R.id.ivdProfile);
        ivMedia = (ImageView) findViewById(R.id.ivMedia);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        Log.d("TweetDetailsActivity", String.format("Showing details for '%s'", tweet.user));

        // set the title and overview
        tvdUser.setText(tweet.user.name);
        tvdBody.setText(tweet.body);
        tvdCreatedAt.setText(tweet.createdAt);
        Glide.with(getApplicationContext()).load(tweet.user.profileImageUrl).into(ivdProfile);
        Glide.with(getApplicationContext()).load(tweet.mediaUrl).into(ivMedia);
    }

    public void onRetweet(View v) {
        client = TwitterApp.getRestClient(getApplicationContext());
        client.reTweet(tweet.uid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                //Tweet tweet = Tweet.fromJSON(response);
                Intent i = new Intent(TweetDetailActivity.this, TimelineActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("sendTweet", "sendtweet failed");
            }
        });
    }
}