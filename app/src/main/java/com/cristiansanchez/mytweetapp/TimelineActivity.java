package com.cristiansanchez.mytweetapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cristiansanchez.mytweetapp.adapters.TweetAdapter;
import com.cristiansanchez.mytweetapp.entities.TweetEntity;
import com.cristiansanchez.mytweetapp.fragments.ComposeTweetFragment;
import com.cristiansanchez.mytweetapp.fragments.ReplyTweetFragment;
import com.cristiansanchez.mytweetapp.listeners.EndlessRecyclerViewScrollListener;
import com.cristiansanchez.mytweetapp.models.Tweet;
import com.cristiansanchez.mytweetapp.network.NetworkUtil;
import com.cristiansanchez.mytweetapp.services.AddTweetService;
import com.cristiansanchez.mytweetapp.utils.DividerItemDecorator;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.greendao.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetFragment.ComposeTweeDialogListener,ReplyTweetFragment.ReplyTweetDialogListener{
    private static final String TAG = "TimelineActivity";
    private SwipeRefreshLayout swipeContainer;
    private RestClient client;
    private TweetAdapter adapter;
    private LinkedList<Tweet> listTweets;
    private RecyclerView rvTweets;
    RecyclerView.LayoutManager layoutManager;
    private final int REQUEST_CODE = 200;
    private NetworkUtil networkUtil;
    private Context context;
    private List<TweetEntity> listTweetsDb;

    long maxId=0;
    long sinceId=0;
    boolean refresh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        context = this;
        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setLogo(R.mipmap.twittericon);
        setSupportActionBar(toolbar);
        networkUtil = new NetworkUtil();
        client = RestApplication.getRestClient(); //Singleton client
        setupViews();
        configTheSwipeRefreshLayout();

        if(networkUtil.connectionPermitted(context)){
            populateTimeline(0,sinceId,maxId);
        }
        else{
            networkUtil.openDataBase(this);
            Query<TweetEntity> query = networkUtil.getTweetDao().queryBuilder().build();
            listTweetsDb = query.list();
            createList(listTweetsDb);
            adapter.notifyItemRangeInserted(adapter.getItemCount(),listTweets.size()-1);
            networkUtil.closeDataBase();
        }

    }

    public void setupViews(){
        //Find the List view
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        //Declare de list
        listTweets = new LinkedList<Tweet>();
        //We create our linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        rvTweets.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecorator(this, DividerItemDecorator.VERTICAL_LIST);
        rvTweets.addItemDecoration(itemDecoration);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("LISTENER","page onScroll: "+page);
                Log.d("LISTENER","totalItemsCount: "+totalItemsCount);
                Log.d("LISTENER","getItemAcount: "+adapter.getItemCount());
                if(networkUtil.connectionPermitted(context)){
                    populateTimeline(page,0,maxId);
                }

            }
        });

        adapter = new TweetAdapter(TimelineActivity.this, listTweets, getSupportFragmentManager());
        rvTweets.setAdapter(adapter);

    }

    //Method to set up our Swipe Refresh Layout
    public void configTheSwipeRefreshLayout(){
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        if(networkUtil.connectionPermitted(context)){
            swipeContainer.setEnabled(true);
            //Getting the SwipeRefreshLayou
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    refresh=true;
                    populateTimeline(1,sinceId,0);
                }

            });

            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }
        else{
            swipeContainer.setEnabled(false);
        }
    }



    //Send an APi request to get the Timeline json and automatically fill the listview by creating the
    //tweet object from the json
    private void populateTimeline(int pageAux, long since_id, long max_id) {
        final List<Tweet> listAux = new ArrayList<Tweet>();
        final int page = pageAux;

        //this condition allows us to know if We have network connection.
        // Werther or not We manage what to do in any case
        if(networkUtil.connectionPermitted(this)){
            client.getHomeTimeline(since_id,max_id,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if(page == 0){
                        listTweets.clear();
                        adapter.notifyDataSetChanged();
                    }


                    if (listTweets.size() > 0) {
                        listAux.addAll(listTweets);
                        listTweets.clear();
                    }
                    ArrayList<Tweet> newArray = Tweet.fromJSONArray(response, listAux,refresh);
                    listTweets.addAll(newArray);
                    launchService(newArray);
                    //Log.d(TAG,"Last item in list: "+(listTweets.size()-1)+" | Last Id: "+listTweets.get(listTweets.size()-1).getuId());
                    int lastTweet = listTweets.size()-1;
                    maxId = (listTweets.get(lastTweet).getuId())-Long.parseLong("1");
                    sinceId = (listTweets.get(0).getuId());
                    Log.d(TAG,"Last ID: "+maxId);
                    Log.d(TAG,"First ID: "+sinceId);

                    TimelineActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh = false;
                            adapter.notifyDataSetChanged();
                            swipeContainer.setRefreshing(refresh);
                        }
                    });

                    Log.d("LIST SIZE", " List size is:" + listTweets.size());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG",errorResponse.toString());
                }
            });
        }
        else{
            Toast.makeText(TimelineActivity.this, "The device has not connection", Toast.LENGTH_SHORT).show();

        }



    }


    // METHODS to set up the tool bar and its menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.compose).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.compose:
                composeTweet();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void composeTweet() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance();
        composeTweetFragment.show(fm,"Fragment layout");
    }

    //This method startup the service to add the tweets to the DataBase
    // to startup the service
    public void launchService(ArrayList<Tweet> list) {
        Intent i = new Intent(this, AddTweetService.class);
        // Add extras to the bundle
        i.putExtra("tweets", Parcels.wrap(list));
        // Start the service
        startService(i);
    }

    public void createList(List<TweetEntity> list){
        for (TweetEntity tweetEntity : list) {
            Tweet tweet = new Tweet(tweetEntity.getBody(), tweetEntity.getUId(), tweetEntity.getCreatedAt(), tweetEntity.getName(),
                    tweetEntity.getUserId(), tweetEntity.getScreenName(), tweetEntity.getProfileImageUrl());
            listTweets.add(tweet);
        }
    }

    /* ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
            listTweets = tweet.addTweetToList(tweet,listTweets);
                TimelineActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refresh = false;
                        adapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(refresh);
                    }
                });
        }
    }
*/

    @Override
    public void onFinishComposeDialog(Tweet tweet) {
        listTweets.addFirst(tweet);
        adapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }

    @Override
    public void onFinishReplyTweetDialog(Tweet tweet) {
        listTweets.addFirst(tweet);
        adapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }
}
