package com.cristiansanchez.mytweetapp.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kristianss27 on 10/26/16.
 */

/* Excample of the json file
* {
    "coordinates": null,
    "truncated": false,
    "created_at": "Tue Aug 28 21:16:23 +0000 2012",
    "favorited": false,
    "id_str": "240558470661799936",
    "in_reply_to_user_id_str": null,
    "entities": {
      "urls": [

      ],
      "hashtags": [

      ],
      "user_mentions": [

      ]
    },
    "text": "just another test",
    "contributors": null,
    "id": 240558470661799936,
    "retweet_count": 0,
    "in_reply_to_status_id_str": null,
    "geo": null,
    "retweeted": false,
    "in_reply_to_user_id": null,
    "place": null,
    "source": "<a href="//realitytechnicians.com%5C%22" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
    "user": {
      "name": "OAuth Dancer",
      "profile_sidebar_fill_color": "DDEEF6",
      "profile_background_tile": true,
      "profile_sidebar_border_color": "C0DEED",
      "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
      "created_at": "Wed Mar 03 19:37:35 +0000 2010",
      "location": "San Francisco, CA",
      "follow_request_sent": false,
      "id_str": "119476949",
      "is_translator": false,
      "profile_link_color": "0084B4",
      "entities": {
        "url": {
          "urls": [
            {
              "expanded_url": null,
              "url": "http://bit.ly/oauth-dancer",
              "indices": [
                0,
                26
              ],
              "display_url": null
            }
          ]
        },
        "description": null
      },
      "default_profile": false,
      "url": "http://bit.ly/oauth-dancer",
      "contributors_enabled": false,
      "favourites_count": 7,
      "utc_offset": null,
      "profile_image_url_https": "https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
      "id": 119476949,
      "listed_count": 1,
      "profile_use_background_image": true,
      "profile_text_color": "333333",
      "followers_count": 28,
      "lang": "en",
      "protected": false,
      "geo_enabled": true,
      "notifications": false,
      "description": "",
      "profile_background_color": "C0DEED",
      "verified": false,
      "time_zone": null,
      "profile_background_image_url_https": "https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
      "statuses_count": 166,
      "profile_background_image_url": "http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",
      "default_profile_image": false,
      "friends_count": 14,
      "following": false,
      "show_all_inline_media": false,
      "screen_name": "oauth_dancer"
    },
    "in_reply_to_screen_name": null,
    "in_reply_to_status_id": null
  },
*/
@Parcel
public class Tweet{
    String body;
    long uId;
    User user;
    String createdAt;

    public Tweet(){
        super();
    }

    public static Tweet parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        Tweet tweet = gson.fromJson(response, Tweet.class);
        return tweet;
    }

    public ArrayList<Tweet> addTweetToList(Tweet tweet, List<Tweet> listTweet){
        ArrayList<Tweet> arrayList = new ArrayList<Tweet>();
        arrayList.add(tweet);
        arrayList.addAll(listTweet);
        return arrayList;
    }

    public Tweet (JSONObject jsonObject) throws JSONException {
        // We are going to extract the values from the JsonObject
        this.body = jsonObject.getString("text");
        this.uId = jsonObject.getLong("id");
        this.createdAt = jsonObject.getString("created_at");
        this.user = new User(jsonObject.getJSONObject("user"));
    }

    public Tweet(String body, long uId, String createdAt,
                       String name, long userId, String screenName,
                       String profileImageUrl) {
        this.body = body;
        this.uId = uId;
        this.createdAt = createdAt;
        User user = new User();
        user.setName(name);
        user.setuId(userId);
        user.setScreenName(screenName);
        user.setProfileImageUrl(profileImageUrl);
        this.user=user;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray, List<Tweet> listTweet,boolean refresh){
        if(jsonArray!=null && jsonArray.length()>0) {

            ArrayList<Tweet> arrayTweet;
            ArrayList<Tweet> arrayTweetRefreshed = new ArrayList<Tweet>();

            if (listTweet.size() > 0) {
                arrayTweet = new ArrayList<Tweet>();
                arrayTweet.addAll(listTweet);
            } else {
                arrayTweet = new ArrayList<Tweet>();
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                try {

                    Tweet tweet = new Tweet(jsonArray.getJSONObject(i));
                    if (tweet != null) {
                        if (refresh) {
                            arrayTweetRefreshed.add(tweet);
                        } else {
                            arrayTweet.add(tweet);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }
            }

            if (refresh) {
                arrayTweetRefreshed.addAll(arrayTweet);
                return arrayTweetRefreshed;
            }

            return arrayTweet;

        }
        return new ArrayList<>(listTweet);

    }

    public String getBody() {
        return body;
    }

    public long getuId() {
        return uId;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
