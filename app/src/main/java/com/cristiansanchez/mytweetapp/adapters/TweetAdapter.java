package com.cristiansanchez.mytweetapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cristiansanchez.mytweetapp.R;
import com.cristiansanchez.mytweetapp.fragments.ReplyTweetFragment;
import com.cristiansanchez.mytweetapp.holders.ViewHolder1;
import com.cristiansanchez.mytweetapp.holders.ViewHolder2;
import com.cristiansanchez.mytweetapp.models.ExtendedEntities;
import com.cristiansanchez.mytweetapp.models.Media;
import com.cristiansanchez.mytweetapp.models.Tweet;
import com.cristiansanchez.mytweetapp.models.Url;
import com.cristiansanchez.mytweetapp.utils.PatternEditableBuilder;
import com.cristiansanchez.mytweetapp.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by kristianss27 on 10/27/16.
 */
public class TweetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //List of any object to display in my RecyclerView
    private List<Tweet> tweets;
    private Context context;
    private ViewHolder1 viewHolder1;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private AdapterListener adapterListener;

    //Type of objects we might use
    private final int VIEWTYPE1 = 0, VIEWTYPE2 = 1;

    public TweetAdapter(Context context, List<Tweet> list, FragmentManager fragmentManager, Fragment fragment, AdapterListener adapterListener) {
        this.context = context;
        this.tweets = list;
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        this.adapterListener = adapterListener;
    }

    //Defining the event that the fragment will use to communicate
    public interface AdapterListener{
        void onUserInfo(String screenName);
        void onRetweet(boolean retweet,long idTweet);
        void onFavorite(boolean favorite, long idTweet);
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

    private void configureViewHolder1(final ViewHolder1 vh1, int position) {
        final Tweet tweet = (Tweet) tweets.get(position);
        if (tweet != null) {
            // Set item views based on your views and data model
            vh1.tvUserName.setText(tweet.getUser().getName());
            vh1.tvUserScreenName.setText("@"+tweet.getUser().getScreenName());
            vh1.tvCreatedAt.setText(new Util().getRelativeTimeAgo(tweet.getCreatedAt()));
            vh1.ivUserImg.setImageResource(0);
            vh1.ivRetweet.setTag(false);
            vh1.ivFavorite.setTag(false);
            vh1.ivEmbeddedImage.setVisibility(View.GONE);

            Picasso.with(getContext())
                    .load(tweet.getUser().getProfileImageUrl())
                    .fit()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(vh1.ivUserImg);

            new PatternEditableBuilder().addPattern(Pattern.compile("\\@(\\w+)"), Color.GRAY,
                    new PatternEditableBuilder.SpannableClickedListener() {
                        @Override
                        public void onSpanClicked(String text) {
                            adapterListener.onUserInfo(text);
                        }
                    }).into(vh1.tvTweetText);


            vh1.ivReplyTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReplyTweetFragment replyTweetFragment = ReplyTweetFragment.newInstance(tweet);
                    replyTweetFragment.setTargetFragment(fragment,200);
                    replyTweetFragment.show(fragmentManager,"Fragment");
                }
            });

            vh1.ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean retweeted = (boolean) vh1.ivRetweet.getTag();
                    if(retweeted)
                    {
                        vh1.ivRetweet.setImageResource(R.drawable.retweet);
                        vh1.ivRetweet.setTag(false);
                        adapterListener.onRetweet(false,tweet.getuId());
                    }
                    else{
                        vh1.ivRetweet.setImageResource(R.drawable.retweeton);
                        vh1.ivRetweet.setTag(true);
                        adapterListener.onRetweet(true,tweet.getuId());
                    }

                }
            });

            vh1.ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean favorite = (boolean) vh1.ivFavorite.getTag();
                    if(favorite)
                    {
                        vh1.ivFavorite.setImageResource(R.drawable.love);
                        vh1.ivFavorite.setTag(false);
                        adapterListener.onFavorite(false,tweet.getuId());
                    }
                    else{
                        vh1.ivFavorite.setImageResource(R.drawable.favorite);
                        vh1.ivFavorite.setTag(true);
                        adapterListener.onFavorite(true,tweet.getuId());
                    }

                }
            });

            vh1.ivUserImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterListener.onUserInfo(tweet.getUser().getScreenName());
                }
            });

            String tweetBody = tweet.getBody();
            List<Url> urls = tweet.getEntities().getUrls();
            if(urls!=null){
                for (Url url : urls) {
                    String urlString = url.getUrl();
                    if(tweet.getBody().contains(urlString) && urlString.contains("/t.co")){
                        tweetBody = tweetBody.replace(urlString,url.getExpandedUrl());
                    }
                }
            }

            ExtendedEntities extendedEntities = tweet.getExtendEntities();
            if(extendedEntities!=null){
                List<Media> mediaList = extendedEntities.getMedia();
                for (int i = 0; i<mediaList.size(); i++){
                    Media media = mediaList.get(i);
                    if(i==0){
                        String urlMedia = media.getMediaUrlHttps();
                        Picasso.with(getContext())
                                .load(urlMedia)
                                .fit()
                                .into(vh1.ivEmbeddedImage);
                        vh1.ivEmbeddedImage.setVisibility(View.VISIBLE);
                    }
                    tweetBody = tweetBody.replace(media.getUrl(),media.getDisplayUrl());
                }
            }

            vh1.tvTweetText.setText(tweetBody);
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
