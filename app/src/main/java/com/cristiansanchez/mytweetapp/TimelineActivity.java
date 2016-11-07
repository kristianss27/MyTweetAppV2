package com.cristiansanchez.mytweetapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.cristiansanchez.mytweetapp.activities.UserInfoActivity;
import com.cristiansanchez.mytweetapp.fragments.MentionsTimelineFragment;
import com.cristiansanchez.mytweetapp.fragments.TweetsListFragment;
import com.cristiansanchez.mytweetapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private static final String TAG = "TimelineActivity";
    RestClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //Get the client
        client = RestApplication.getRestClient();

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.parseJSON(response.toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if(errorResponse!=null){
                    Log.d(TAG,"Error:"+errorResponse);
                }
            }
        });


        toolbar.setLogo(R.mipmap.twittericon);
        setSupportActionBar(toolbar);

        //Get the view pager and set its PageAdapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        //Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(viewPager);


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
        menu.findItem(R.id.profile).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                goUserInfoActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goUserInfoActivity(){
        Intent intent = new Intent(TimelineActivity.this, UserInfoActivity.class);
        intent.putExtra("user", Parcels.wrap(user));
        startActivity(intent);
    }

    //Returns the fragment according to the tab name in the view pager
    public class PagerAdapter extends FragmentPagerAdapter{
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { "Home", "Mentions"};

        //We should create the constructor to get the FragmentManager
        public PagerAdapter(FragmentManager fm){
            super(fm);
        }

        //And Override the methods that allow us to get how many tabs we have and wich Fragment will show off with them

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return new TweetsListFragment();
            }
            else if (position==1){
                return new MentionsTimelineFragment();
            }
            else{
                return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
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


}
