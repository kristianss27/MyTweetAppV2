package com.cristiansanchez.mytweetapp.holders;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cristiansanchez.mytweetapp.R;
import com.cristiansanchez.mytweetapp.TweetDetailActivity;
import com.cristiansanchez.mytweetapp.models.Tweet;
import com.cristiansanchez.mytweetapp.utils.PatternEditableBuilder;

import org.parceler.Parcels;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by kristianss27 on 10/27/16.
 */
public class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView ivUserImg;
    public ImageView ivReplyTweet;
    public TextView tvUserName;
    public TextView tvUserScreenName;
    public TextView tvTweetText;
    public TextView tvCreatedAt;
    private Context context;
    private List<Tweet> tweets;

    public ViewHolder1(View v, final Context context, List<Tweet> tweets){
        super(v);
        ivUserImg = (ImageView) v.findViewById(R.id.ivUserImg);
        ivReplyTweet = (ImageView) v.findViewById(R.id.ivReply);
        tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        tvTweetText = (TextView) v.findViewById(R.id.tvTweetText);
        tvUserScreenName = (TextView) v.findViewById(R.id.tvUserScreenName);
        tvCreatedAt = (TextView) v.findViewById(R.id.tvCreatedAt);
        tvTweetText.setLinkTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.twitterLogo)));

        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {

                            }
                        }).into(tvTweetText);
        // Store the context
        this.context = context;
        this.tweets = tweets;
        // Attach a click listener to the entire row view
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition(); // gets item position
        if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
            Tweet tweet = tweets.get(position);
            // We can access the data within the views
            Intent intent = new Intent(context, TweetDetailActivity.class);
            intent.putExtra("tweet", Parcels.wrap(tweet));
            v.getContext().startActivity(intent);

        }
    }
}
