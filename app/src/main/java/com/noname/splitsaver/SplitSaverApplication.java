package com.noname.splitsaver;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.noname.splitsaver.Network.NetworkManager;

public class SplitSaverApplication extends Application {

    private static final String KEY_LOGGED_IN = "keyLoggedIn";

    public static void login(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.apply();
    }

    public static void logout(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_LOGGED_IN, false);
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(KEY_LOGGED_IN, false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.init();
    }
}
