package com.cristiansanchez.mytweetapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.cristiansanchez.mytweetapp.R;
import com.cristiansanchez.mytweetapp.fragments.UserTimelineFragment;
import com.cristiansanchez.mytweetapp.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoActivity extends AppCompatActivity {
    @BindView(R.id.ivUserBackground) ImageView ivUserBackground;
    @BindView(R.id.ivUserImg) ImageView ivUserImg;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvUserScreenName) TextView tvUserScreenName;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.tvDescription) TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        //Get the screen_name from the TimelineActivity
        User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        String userImgBackgroundUrl =(user.getIsProfileBackgroundImage().equalsIgnoreCase("true"))?user.getProfileBannerUrl():null;
        //Set all the views from the user object
        Picasso.with(this)
                .load(user.getProfileImageUrl())
                .fit()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(ivUserImg);

        Picasso.with(this)
                .load(userImgBackgroundUrl)
                .fit()
                .into(ivUserBackground);

        tvUserName.setText(user.getName());
        tvUserScreenName.setText("@"+user.getScreenName());
        tvFollowers.setText(user.getFollowersCount());
        tvFollowing.setText(user.getFriendsCount());
        tvDescription.setText(user.getDescription());

        String screen_name = user.getScreenName();
        if(savedInstanceState==null) {
            //Create the UserTimelineFragment
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screen_name);
            //Display the UserTimelineFragment in the Activity (dinamically)
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flUserTimeline, userTimelineFragment);
            fragmentTransaction.commit();
        }
    }

}
