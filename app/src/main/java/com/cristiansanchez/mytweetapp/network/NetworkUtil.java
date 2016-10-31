package com.cristiansanchez.mytweetapp.network;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.cristiansanchez.mytweetapp.entities.DaoMaster;
import com.cristiansanchez.mytweetapp.entities.DaoSession;
import com.cristiansanchez.mytweetapp.entities.TweetEntityDao;

import java.io.IOException;

/**
 * Created by kristianss27 on 10/30/16.
 */
public class NetworkUtil {
    private static final String TAG = "NetworkUtil";
    private static NetworkUtil networkUtil;
    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase dataBase;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TweetEntityDao tweetDao;

    public static synchronized NetworkUtil getInstance() {
        if ( networkUtil == null ) {
            networkUtil = new NetworkUtil();
        }
        return networkUtil;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void openDataBase(Context context) {
        Log.d(TAG,"Start opening database");
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,"my-tweet-app-db", null);
        if (dataBase == null) {
            dataBase = helper.getWritableDatabase();
            Log.e("DATA BASE: ", helper.getDatabaseName());
        } else {
            if (!dataBase.isOpen()) {
                dataBase = helper.getWritableDatabase();
                Log.e("DATA BASE", "Is open: " + dataBase.isOpen());
            } else {
                Log.e("DATA BASE", "Is open: " + dataBase.isOpen());
            }

        }
        daoMaster = new DaoMaster(dataBase);
        daoSession = daoMaster.newSession();
        tweetDao = daoSession.getTweetEntityDao();
    }

    public void closeDataBase() {
        daoSession.clear();
        dataBase.close();
    }


    public boolean connectionPermitted(Context context){
        return isNetworkAvailable(context) && isOnline();
    }

    private Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    public TweetEntityDao getTweetDao() {
        return tweetDao;
    }

    public void setTweetDao(TweetEntityDao tweetDao) {
        this.tweetDao = tweetDao;
    }

}
