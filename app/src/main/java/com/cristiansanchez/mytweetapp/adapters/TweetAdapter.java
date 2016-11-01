package com.cristiansanchez.mytweetapp.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cristiansanchez.mytweetapp.R;
import com.cristiansanchez.mytweetapp.fragments.ReplyTweetFragment;
import com.cristiansanchez.mytweetapp.holders.ViewHolder1;
import com.cristiansanchez.mytweetapp.holders.ViewHolder2;
import com.cristiansanchez.mytweetapp.models.Tweet;
import com.cristiansanchez.mytweetapp.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kristianss27 on 10/27/16.
 */
public class TweetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //List of any object to display in my RecyclerView
    private List<Tweet> tweets;
    private Context context;
    private ViewHolder1 viewHolder1;
    private FragmentManager fragmentManager;

    //Type of objects we might use
    private final int VIEWTYPE1 = 0, VIEWTYPE2 = 1;

    public TweetAdapter(Context context, List<Tweet> list, FragmentManager fragmentManager) {
        this.context = context;
        this.tweets = list;
        this.fragmentManager = fragmentManager;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return this.tweets.size();
    }

    @Override
    public int getItemViewType(int position) {
        /*if (!tweets.get(position).getMultimedia().equalsIgnoreCase("")) {
            return IMAGE;
        }*/
        return VIEWTYPE1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEWTYPE1:
                View v1 = inflater.inflate(R.layout.layout_viewholder1, parent, false);
                viewHolder = new ViewHolder1(v1,parent.getContext(),tweets);
                break;
            case VIEWTYPE2:
                View v2 = inflater.inflate(R.layout.layout_viewholder2, parent, false);
                viewHolder = new ViewHolder2(v2,parent.getContext(),tweets);
                break;
            default:
                View v3 = inflater.inflate(R.layout.layout_viewholder2, parent, false);
                viewHolder = new ViewHolder2(v3,parent.getContext(),tweets);
                break;
        }

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Tweet tweet = tweets.get(position);

        switch (viewHolder.getItemViewType()) {
            case VIEWTYPE1:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case VIEWTYPE2:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            default:
                ViewHolder2 vh3 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh3, position);
                break;
        }


    }

    private void configureViewHolder1(ViewHolder1 vh1, int position) {
        final Tweet tweet = (Tweet) tweets.get(position);
        if (tweet != null) {
            // Set item views based on your views and data model
            vh1.tvUserName.setText(tweet.getUser().getName());
            vh1.tvUserScreenName.setText("@"+tweet.getUser().getScreenName());
            vh1.tvTweetText.setText(tweet.getBody());
            vh1.tvCreatedAt.setText(new Util().getRelativeTimeAgo(tweet.getCreatedAt()));
            vh1.ivUserImg.setImageResource(0);

            Picasso.with(getContext())
                    .load(tweet.getUser().getProfileImageUrl())
                    .fit()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(vh1.ivUserImg);

            vh1.ivReplyTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReplyTweetFragment replyTweetFragment = ReplyTweetFragment.newInstance(tweet);
                    replyTweetFragment.show(fragmentManager,"Fragment");
                }
            });
        }
    }

    private void configureViewHolder2(ViewHolder2 vh2, int position) {
        Tweet tweet = (Tweet) tweets.get(position);
        if (tweet != null) {
            // Set item views based on your views and data model
            vh2.tvUserName.setText(tweet.getUser().getName());
            vh2.tvTweetText.setText(tweet.getBody());
            vh2.ivUserImg.setImageResource(0);

            Picasso.with(getContext())
                    .load(R.mipmap.ic_launcher)
                    .fit()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(vh2.ivUserImg);
        }

    }

}
