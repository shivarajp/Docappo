package com.ndtv.ndtvdoc;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Shivam on 4/12/2016.
 */
public class NDTVApp extends Application {

    @Override
    public void onCreate() {
        Firebase.setAndroidContext(this);
        super.onCreate();

    }
}
