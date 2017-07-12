package com.noname.splitsaver;

import android.app.Application;

import com.noname.splitsaver.Network.NetworkManager;

public class SplitSaverApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.init();
    }
}
