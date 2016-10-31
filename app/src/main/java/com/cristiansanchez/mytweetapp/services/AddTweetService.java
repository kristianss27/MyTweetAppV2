package com.cristiansanchez.mytweetapp.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cristiansanchez.mytweetapp.entities.TweetEntity;
import com.cristiansanchez.mytweetapp.entities.TweetEntityDao;
import com.cristiansanchez.mytweetapp.models.Tweet;
import com.cristiansanchez.mytweetapp.network.NetworkUtil;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kristianss27 on 10/30/16.
 */
public class AddTweetService extends IntentService{
    Context context;

    public AddTweetService() {
        super("AddTweetService");
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("SERVICE","Service running");
        ArrayList<Tweet> tweets = (ArrayList<Tweet>) Parcels.unwrap(intent.getParcelableExtra("tweets"));
        Log.d("SERVICE","Tweets: "+tweets.size());
        NetworkUtil networkUtil = NetworkUtil.getInstance();
        networkUtil.openDataBase(this);
        for (Tweet tweet : tweets) {
            TweetEntityDao tweetDao = networkUtil.getDaoSession().getTweetEntityDao();
            List tweetList = tweetDao.queryBuilder().where(TweetEntityDao.Properties.UId.eq(tweet.getuId())).list();
            if(tweetList.size()>0){
                Log.d("AddTweetService", "Tweet inserted before");
            }
            else {
                TweetEntity tweetEntity = new TweetEntity(null, tweet.getBody(), tweet.getuId(), tweet.getCreatedAt(), tweet.getUser().getName(),
                        tweet.getUser().getuId(), tweet.getUser().getScreenName(), tweet.getUser().getProfileImageUrl());

                tweetDao.insert(tweetEntity);
                Log.d("AddTweetService", "Inserted new tweet, ID: " + tweetEntity.getId());
            }
        }
        networkUtil.closeDataBase();
        // This describes what will happen when service is triggered
    }
}
