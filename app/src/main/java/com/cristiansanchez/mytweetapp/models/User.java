package com.cristiansanchez.mytweetapp.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("id")
    long uId;
    @SerializedName("screen_name")
    String screenName;
    @SerializedName("followers_count")
    String followersCount;
    @SerializedName("following")
    String following;
    @SerializedName("friends_count")
    String friendsCount;
    @SerializedName("profile_image_url")
    String profileImageUrl;
    @SerializedName("profile_background_image_url")
    String profileBackgroundImageUrl;
    @SerializedName("profile_use_background_image")
    String isProfileBackgroundImage;
    @SerializedName("profile_background_color")
    String profileBackgroundColor;
    String description;
    @SerializedName("profile_banner_url")
    String profileBannerUrl;

    public User(){
        super();
    }

    public static User parseJSON(String response){
        User user = null;
        //We instance the GsonBuilder
        GsonBuilder gsonBuilder = new GsonBuilder();
        //We already have an object called Tweet where We decode string and other values.Now We want to pass a type that defines that object
        Gson gson = gsonBuilder.create();
        try{
            user = gson.fromJson(response,User.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public String getName() {
        return name;
    }

    public long getuId() {
        return uId;
    }

    public String getScreenName() { return screenName; }

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

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public void setProfileBackgroundColor(String profileBackgroundColor) {
        this.profileBackgroundColor = profileBackgroundColor;
    }

    public String getIsProfileBackgroundImage() {
        return isProfileBackgroundImage;
    }

    public void setIsProfileBackgroundImage(String isProfileBackgroundImage) {
        this.isProfileBackgroundImage = isProfileBackgroundImage;
    }

    public String getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(String friendsCount) {
        this.friendsCount = friendsCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public void setProfileBannerUrl(String profileBannerUrl) {
        this.profileBannerUrl = profileBannerUrl;
    }
}

