package com.cristiansanchez.mytweetapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cristiansanchez.mytweetapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeTweetActivity extends AppCompatActivity {
    RestClient client;
    private EditText etTweet;
    private TextView tvCharacterCounter;
    private Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        etTweet = (EditText) findViewById(R.id.etTweet);
        tvCharacterCounter = (TextView) findViewById(R.id.tvCharacterCounter);
        client = RestApplication.getRestClient(); //Singleton client
    }

    public void composeTweet(View view) {
        EditText etTweet = (EditText) findViewById(R.id.etTweet);

        client.composeTweet(etTweet.getText().toString(), null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("COMPOSE TWEET","Compose tweet response: "+responseString);
                Log.d("COMPOSE TWEET","LLEGOOO");
                tweet = Tweet.parseJSON(responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("ComposeTweetFragment","Tweet failed");
            }

            });

        //We should create an Intent to set the result
        Intent data = new Intent();
        data.putExtra("tweet", Parcels.wrap(tweet));
        data.putExtra("code", 200);
        // Activity finished ok, return the data. Set result code and bundle data for response
        setResult(RESULT_OK, data);
        // closes the activity, pass data to parent
        finish();

    }
}
