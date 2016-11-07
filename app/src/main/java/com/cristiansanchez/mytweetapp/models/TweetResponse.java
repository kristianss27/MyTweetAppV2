package com.cristiansanchez.mytweetapp.models;

/**
 * Created by kristianss27 on 10/29/16.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/** Example Json returned
 * {
 "coordinates": null,
 "favorited": false,
 "created_at": "Wed Sep 05 00:37:15 +0000 2012",
 "truncated": false,
 "id_str": "243145735212777472",
 "entities": {
 "urls": [

 ],
 "hashtags": [
 {
 "text": "peterfalk",
 "indices": [
 35,
 45
 ]
 }
 ],
 "user_mentions": [

 ]
 },
 "in_reply_to_user_id_str": null,
 "text": "Maybe he'll finally find his keys. #peterfalk",
 "contributors": null,
 "retweet_count": 0,
 "id": 243145735212777472,
 "in_reply_to_status_id_str": null,
 "geo": null,
 "retweeted": false,
 "in_reply_to_user_id": null,
 "place": null,
 "user": {
 "name": "Jason Costa",
 "profile_sidebar_border_color": "86A4A6",
 "profile_sidebar_fill_color": "A0C5C7",
 "profile_background_tile": false,
 "profile_image_url": "http://a0.twimg.com/profile_images/1751674923/new_york_beard_normal.jpg",
 "created_at": "Wed May 28 00:20:15 +0000 2008",
 "location": "",
 "is_translator": true,
 "follow_request_sent": false,
 "id_str": "14927800",
 "profile_link_color": "FF3300",
 "entities": {
 "url": {
 "urls": [
 {
 "expanded_url": "http://www.jason-costa.blogspot.com/",
 "url": "http://t.co/YCA3ZKY",
 "indices": [
 0,
 19
 ],
 "display_url": "jason-costa.blogspot.com"
 }
 ]
 },
 "description": {
 "urls": [

 ]
 }
 },
 "default_profile": false,
 "contributors_enabled": false,
 "url": "http://t.co/YCA3ZKY",
 "favourites_count": 883,
 "utc_offset": -28800,
 "id": 14927800,
 "profile_image_url_https": "https://si0.twimg.com/profile_images/1751674923/new_york_beard_normal.jpg",
 "profile_use_background_image": true,
 "listed_count": 150,
 "profile_text_color": "333333",
 "protected": false,
 "lang": "en",
 "followers_count": 8760,
 "time_zone": "Pacific Time (US & Canada)",
 "profile_background_image_url_https": "https://si0.twimg.com/images/themes/theme6/bg.gif",
 "verified": false,
 "profile_background_color": "709397",
 "notifications": false,
 "description": "Platform at Twitter",
 "geo_enabled": true,
 "statuses_count": 5532,
 "default_profile_image": false,
 "friends_count": 166,
 "profile_background_image_url": "http://a0.twimg.com/images/themes/theme6/bg.gif",
 "show_all_inline_media": true,
 "screen_name": "jasoncosta",
 "following": false
 },
 "source": "My Shiny App",
 "in_reply_to_screen_name": null,
 "in_reply_to_status_id": null
 }
 */
public class TweetResponse {

    private static List<Tweet> tweets;

    public TweetResponse(){}

    public static List<Tweet> parseJSON(String response){
        //We instance the GsonBuilder
        GsonBuilder gsonBuilder = new GsonBuilder();
        //We already have an object called Tweet where We decode string and other values.Now We want to pass a type that defines that object
        Type tweetType = new TypeToken<List<Tweet>>(){}.getType();
        Gson gson = gsonBuilder.create();
        //In this case we need to get a Tweet List
        List<Tweet> tweetList = gson.fromJson(response,tweetType);
        //In case We do not want to change the name of our variables on the class, we can set the name policy in the Gson Library with the code line below
        //gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        //In case You just need to get one response You should use the code below
        // TweetResponse tweetResponse = gson.fromJson(response,TweetResponse.class);

        return tweetList;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }
}
