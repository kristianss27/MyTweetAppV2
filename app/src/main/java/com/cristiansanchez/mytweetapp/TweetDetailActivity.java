package com.cristiansanchez.mytweetapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.cristiansanchez.mytweetapp.models.Tweet;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {
    private static final String TAG = "TimelineActivity";
    public ImageView ivUserImg;
    public TextView tvUserName;
    public TextView tvUserScreenName;
    public TextView tvTweetText;
    public TextView tvCreatedAt;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setLogo(R.mipmap.twittericon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tweet");

        ivUserImg = (ImageView) findViewById(R.id.ivUserImg);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvTweetText = (TextView) findViewById(R.id.tvTweetText);
        tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenName);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        tvUserName.setText(tweet.getUser().getName());
        tvUserScreenName.setText(tweet.getUser().getScreenName());
        tvTweetText.setText(tweet.getBody());
        ivUserImg.setImageResource(0);

        Picasso.with(this)
                .load(tweet.getUser().getProfileImageUrl())
                .fit()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(ivUserImg);

    }
}
