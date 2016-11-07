package com.cristiansanchez.mytweetapp.utils;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by kristianss27 on 10/27/16.
 */
public class Util {

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo2(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";

        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*String date;
        if(relativeDate.contains("in")){
            String[] array = relativeDate.split(" ");
            date = array[1].toString();
            date = date + array[2].substring(0,1);
        }
        else {
            String[] array = relativeDate.split(" ");
            date = array[0].toString();
            date = date + array[1].substring(0, 1);
        }

        return date;*/
        return relativeDate;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Making the string look like "1h", "1m", etc.
        String[] relativeDateSplitted = relativeDate.split(" ");
        if(relativeDateSplitted[0].contains("in")){
            relativeDateSplitted[0]=relativeDateSplitted[1];
            relativeDateSplitted[1]=relativeDateSplitted[2];
        }
        Log.d("TIME","relative date: "+ relativeDate);
        if (relativeDateSplitted[1].contains("second")) {
            return (relativeDateSplitted[0] + "s");
        }
        else if (relativeDateSplitted[1].contains("minute")) {
            return relativeDateSplitted[0] + "m";
        } else if (relativeDateSplitted[1].contains("hour")) {
            return relativeDateSplitted[0] + "h";
        } else if (relativeDateSplitted[1].contains("day")) {
            return relativeDateSplitted[0] + "d";
        } else if (relativeDateSplitted[1].contains("month")) {
            return relativeDateSplitted[0] + "M";
        } else {
            Log.d("TIME","relative date: "+ relativeDateSplitted[1]);
            return relativeDateSplitted[0] + "y";
        }
    }
}
