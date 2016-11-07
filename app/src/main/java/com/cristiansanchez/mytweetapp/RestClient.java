package com.cristiansanchez.mytweetapp;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 *
 * This is the object responsible for communicating with a REST API.
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes:
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 *
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 *
 */
public class RestClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1/";
    public static final String REST_CONSUMER_KEY = "9490ozygQXO2AmVmQoCDPvsvF";
    public static final String REST_CONSUMER_SECRET = "QEQLG8QzlUiVwE8zRVWHqhvrI0tryycTU2mVninDSitpWrkgLc";
    public static final String REST_CALLBACK_URL = "oauth://mytweetapp";

    public RestClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    //This method is going to set up all the params we need to get the home timeline
    public void getHomeTimeline(long sinceId,long maxId,AsyncHttpResponseHandler handler){
        //We use the url to get the json file
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // Specify the params
        RequestParams params = new RequestParams();
        params.put("count", 15);
        if(sinceId!=0) {
            params.put("since_id", sinceId);
        }
        if(maxId!=0){
            params.put("max_id",maxId);
        }
        //Execute the request
        getClient().get(apiUrl, params, handler);

    }

    //This method is going to set up all the params we need to get the mentions timeline
    public void getMentionsTimeLine(long sinceId,long maxId,AsyncHttpResponseHandler handler){
        //We use the url to get the json file
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        // Specify the params
        RequestParams params = new RequestParams();
        params.put("count", 15);
        if(sinceId!=0) {
            params.put("since_id", sinceId);
        }
        if(maxId!=0){
            params.put("max_id",maxId);
        }
        //Execute the request
        getClient().get(apiUrl, params, handler);

    }

    //This method permits us update our status in twitter. Compose a tweet.
    // Source:https://dev.twitter.com/rest/reference/post/statuses/update

    public void composeTweet(String status,String replyTo,AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        //text of your status updated
        params.put("status",status);
        //The ID of an existing
        // that the update is in reply to. Therefore, you must include @username
        if(replyTo!=null){
            replyTo = "@"+replyTo;
            params.put("in_reply_to_status_id",replyTo);
        }

        getClient().post(apiUrl,params,handler);

        //If you upload Tweet media that might be considered sensitive content such as nudity, violence, or medical procedures, you should set this value to true
        //params.put("possibly_sensitive",true);
        //A list of media_ids to associate with the Tweet. You may include up to 4 photos or 1 animated GIF
        // or 1 video in a Tweet. See: https://dev.twitter.com/rest/media/uploading-media
        //params.put("media_ids","");
    }

    //This methdod allow us to get all the infor from the current user
    public void getUserInfo(AsyncHttpResponseHandler handler){
        //We use the url to get the json file
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        params.put("include_entities",true);
        params.put("include_email", true);
        //Execute the request
        getClient().get(apiUrl,params,handler);
    }

    //This methdod allow us to get all the infor from the current user
    public void getUserInfo(String screen_name, AsyncHttpResponseHandler handler){
        //We use the url to get the json file
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name",screen_name);
        //Execute the request
        getClient().get(apiUrl,params,handler);
    }

    public void getUserTimeline(long sinceId,long maxId,String screen_name,AsyncHttpResponseHandler handler){
        //We use the url to get the json file
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("screen_name",screen_name);
        params.put("count", 15);
        if(sinceId!=0) {
            params.put("since_id", sinceId);
        }
        if(maxId!=0){
            params.put("max_id",maxId);
        }
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

    //This methdod allow us to get all the infor from the current user
    public void retweet(boolean retweet,String id,AsyncHttpResponseHandler handler){
        String action = (retweet)?"retweet":"unretweet";
        //We use the url to get the json file
        String apiUrl = getApiUrl("statuses/"+action+"/"+id+".json");
        //Execute the request
        getClient().post(apiUrl,handler);
    }

    //This methdod allow us to get all the infor from the current user
    public void favorite(boolean favorite,String id,AsyncHttpResponseHandler handler){
        String action = (favorite)?"create":"destroy";
        RequestParams params = new RequestParams();
        params.put("id",id);
        //We use the url to get the json file
        String apiUrl = getApiUrl("favorites/"+action+".json");
        //Execute the request
        getClient().post(apiUrl,params,handler);
    }



}