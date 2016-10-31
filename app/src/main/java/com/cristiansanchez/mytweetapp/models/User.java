package com.cristiansanchez.mytweetapp.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/*
Exampl of the json file
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
 */
@Parcel
public class User {
    String name;
    long uId;
    String screenName;
    String profileImageUrl;


    public User(){
        super();
    }

    public User(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("name");
        this.uId = jsonObject.getLong("id");
        this.screenName = jsonObject.getString("screen_name");
        this.profileImageUrl = jsonObject.getString("profile_image_url");
    }

    public String getName() {
        return name;
    }

    public long getuId() {
        return uId;
    }

    public String getScreenName() {
        return "@"+screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setuId(long uId) {
        this.uId = uId;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

}

