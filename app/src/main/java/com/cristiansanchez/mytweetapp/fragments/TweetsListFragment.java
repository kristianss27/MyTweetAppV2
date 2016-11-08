package com.cristiansanchez.mytweetapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cristiansanchez.mytweetapp.R;
import com.cristiansanchez.mytweetapp.RestApplication;
import com.cristiansanchez.mytweetapp.RestClient;
import com.cristiansanchez.mytweetapp.TimelineActivity;
import com.cristiansanchez.mytweetapp.activities.UserInfoActivity;
import com.cristiansanchez.mytweetapp.adapters.TweetAdapter;
import com.cristiansanchez.mytweetapp.entities.TweetEntity;
import com.cristiansanchez.mytweetapp.listeners.EndlessRecyclerViewScrollListener;
import com.cristiansanchez.mytweetapp.models.Tweet;
import com.cristiansanchez.mytweetapp.models.TweetResponse;
import com.cristiansanchez.mytweetapp.models.User;
import com.cristiansanchez.mytweetapp.network.NetworkUtil;
import com.cristiansanchez.mytweetapp.services.AddTweetService;
import com.cristiansanchez.mytweetapp.utils.DividerItemDecorator;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class TweetsListFragment extends Fragment implements TweetAdapter.AdapterListener,ComposeTweetFragment.ComposeTweeDialogListener,ReplyTweetFragment.ReplyTweetDialogListener{
    private static final String TAG = "TweetsListFragment";
    TimelineActivity listener;

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
    private TweetResponse tweetResponse;
    private FloatingActionButton btnCompose;
    long maxId=0;
    long sinceId=0;
    boolean refresh=false;

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof Activity){
            this.listener = (TimelineActivity) context;
        }
    }

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Instance the TweetResponse
        tweetResponse = new TweetResponse();
        //Declare de list
        listTweets = new LinkedList<Tweet>();
        //This class allows us to check the internet conection and connecting to the db
        networkUtil = new NetworkUtil();
        //Singleton client
        client = RestApplication.getRestClient();

    }


    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list,parent,false);
        return v;
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        btnCompose = (FloatingActionButton) view.findViewById(R.id.btnCompose);
        //Find the Recycler view
        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);
        //We create our linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //Set the Layout Manager in the Recycler View
        rvTweets.setLayoutManager(linearLayoutManager);
        //Creation of ItemDecoration to draw the division line between the tweets
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecorator(getActivity(), DividerItemDecorator.VERTICAL_LIST);
        rvTweets.addItemDecoration(itemDecoration);
        //OnScrollListener to the RecyclerView
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("LISTENER","page onScroll: "+page);
                Log.d("LISTENER","totalItemsCount: "+totalItemsCount);
                Log.d("LISTENER","getItemAcount: "+adapter.getItemCount());
                if(networkUtil.connectionPermitted(context)){
                    populateTimeline(page,0,maxId);
                }
                else{

                }

            }
        });
        // Create and Set up the adapter into de RecyclerView
        adapter = new TweetAdapter(getActivity(), listTweets, getFragmentManager(),this,this);
        rvTweets.setAdapter(adapter);
        //SwipeRefresh Layout necesary to refresh the Recycler View with new tweets
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        configTheSwipeRefreshLayout();

        btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance();
                // SETS the target fragment for use later when sending results
                composeTweetFragment.setTargetFragment(TweetsListFragment.this, 300);
                composeTweetFragment.show(fm,"Fragment layout");
            }
        });

        //Check if there is connection
        if(networkUtil.connectionPermitted(getActivity())){
            populateTimeline(0,sinceId,maxId);
        }
        else{
            Toast.makeText(getActivity(),"You are offline!",Toast.LENGTH_LONG).show();
            /*networkUtil.openDataBase(this);
            Query<TweetEntity> query = networkUtil.getTweetDao().queryBuilder().build();
            listTweetsDb = query.list();
            createList(listTweetsDb);
            adapter.notifyItemRangeInserted(adapter.getItemCount(),listTweets.size()-1);
            networkUtil.closeDataBase();*/
        }
    }

    //Method to set up our Swipe Refresh Layout
    public void configTheSwipeRefreshLayout(){
        if(networkUtil.connectionPermitted(getActivity())){
            swipeContainer.setEnabled(true);
            //Getting the SwipeRefreshLayou
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
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
            Toast.makeText(getActivity(),"Connection Failed",Toast.LENGTH_LONG).show();
            swipeContainer.setEnabled(false);
        }
    }

    //Send an APi request to get the Timeline json and automatically fill the listview by creating the
    //tweet object from the json
    private void populateTimeline(int pageAux, long since_id, long max_id) {
        final int page = pageAux;

        //this condition allows us to know if We have network connection.
        // Werther or not We manage what to do in any case
        if(networkUtil.connectionPermitted(getActivity())){
            client.getHomeTimeline(since_id,max_id,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    final int curSize = adapter.getItemCount();
                    List<Tweet> newArray = TweetResponse.parseJSON(response.toString());
                    final int size = newArray.size();
                    Log.d("LIST SIZE", " List size start in:" + size);

                    if(refresh){
                        if(size>0) {
                            newArray.addAll(listTweets);
                            listTweets.clear();
                        }
                    }

                    listTweets.addAll(newArray);

                    //launchService(newArray);
                    //Log.d(TAG,"Last item in list: "+(listTweets.size()-1)+" | Last Id: "+listTweets.get(listTweets.size()-1).getuId());
                    int lastTweet = listTweets.size()-1;
                    maxId = (listTweets.get(lastTweet).getuId())-Long.parseLong("1");
                    sinceId = (listTweets.get(0).getuId());
                    Log.d(TAG,"Last ID: "+maxId);
                    Log.d(TAG,"First ID: "+sinceId);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(refresh){
                                refresh = false;
                                if(size>0) {
                                    adapter.notifyItemRangeRemoved(0, curSize);
                                    adapter.notifyItemRangeInserted(0, listTweets.size());
                                }
                                swipeContainer.setRefreshing(refresh);

                            }
                            else {
                                refresh = false;
                                adapter.notifyItemRangeInserted(curSize, size);
                                swipeContainer.setRefreshing(refresh);
                            }
                        }
                    });

                    Log.d("LIST SIZE", " List size is:" + listTweets.size());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Toast.makeText(getActivity(),"Connection Failed",Toast.LENGTH_LONG).show();
                    refresh=false;
                    swipeContainer.setRefreshing(false);
                }
            });
        }
        else{
            Toast.makeText(getActivity(), "The device has not connection", Toast.LENGTH_SHORT).show();
        }



    }

    public void createList(List<TweetEntity> list){
        for (TweetEntity tweetEntity : list) {
            Tweet tweet = new Tweet(tweetEntity.getBody(), tweetEntity.getUId(), tweetEntity.getCreatedAt(), tweetEntity.getName(),
                    tweetEntity.getUserId(), tweetEntity.getScreenName(), tweetEntity.getProfileImageUrl());
            listTweets.add(tweet);
        }
    }

    //This method startup the service to add the tweets to the DataBase
    // to startup the service
    public void launchService(List<Tweet> list) {
        Intent i = new Intent(getActivity(), AddTweetService.class);
        // Add extras to the bundle
        i.putExtra("tweets", Parcels.wrap(list));
        // Start the service
        getActivity().startService(i);
    }

    @Override
    public void onFinishComposeDialog(Tweet tweet) {
        refresh=true;
        populateTimeline(1,sinceId,0);
    }

    @Override
    public void onFinishReplyTweetDialog(Tweet tweet) {
        refresh=true;
        populateTimeline(1,sinceId,0);
    }


    @Override
    public void onUserInfo(String screenName) {
        client.getUserInfo(screenName,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.parseJSON(response.toString());
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra("user", Parcels.wrap(user));
                startActivity(intent);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if(errorResponse!=null){
                    Log.d(TAG,"Error:"+errorResponse);
                }
            }
        });
    }

    @Override
    public void onRetweet(boolean retweet,long idTweet) {
        client.retweet(retweet,String.valueOf(idTweet),new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG,"Success:"+response.toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if(errorResponse!=null){
                    Log.d(TAG,"Error:"+errorResponse);
                }
            }
        });
    }

    @Override
    public void onFavorite(boolean favorite,long idTweet) {
        client.favorite(favorite,String.valueOf(idTweet),new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG,"Success:"+response.toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if(errorResponse!=null){
                    Log.d(TAG,"Error:"+errorResponse);
                }
            }
        });
    }
}
