package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
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
    public int fav = 0;

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
                try {
                    Tweet tweet1 = Tweet.fromJSON(response);
                    TimelineActivity.tweets.add(0, tweet1);
                    TimelineActivity.tweetAdapter.notifyItemInserted(0);
                    TimelineActivity.rvTweets.scrollToPosition(0);
                    //Intent i = new Intent(TweetDetailActivity.this, TimelineActivity.class);
                    //startActivity(i);
                    finish();
                } catch (JSONException e) {
                    Log.e("retweet", "retweet failed");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("retweet", "retweet failed");
            }
        });
    }

    public void onFav(View v) {
        client = TwitterApp.getRestClient(getApplicationContext());
        if (fav == 0) {
            client.favTweet(tweet.uid, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //super.onSuccess(statusCode, headers, response);
                    try {
                        fav = 1;
                        Tweet tweet1 = Tweet.fromJSON(response);
                        TimelineActivity.tweets.add(0, tweet1);
                        TimelineActivity.tweetAdapter.notifyItemInserted(0);
                        //Intent i = new Intent(TweetDetailActivity.this, TimelineActivity.class);
                        //startActivity(i);
                        finish();
                    } catch (JSONException e) {
                        Log.e("favtweet", "favtweet failed");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("favtweet", "favtweet failed");
                }
            });
        }
        else {
            client.unfavTweet(tweet.uid, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //super.onSuccess(statusCode, headers, response);
                    try {
                        fav = 0;
                        Tweet tweet1 = Tweet.fromJSON(response);
                        TimelineActivity.tweets.add(0, tweet1);
                        TimelineActivity.tweetAdapter.notifyItemInserted(0);
                        //Intent i = new Intent(TweetDetailActivity.this, TimelineActivity.class);
                        //startActivity(i);
                        finish();
                    } catch (JSONException e) {
                        Log.e("favtweet", "favtweet failed");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("favtweet", "favtweet failed");
                }
            });
        }
    }
}
