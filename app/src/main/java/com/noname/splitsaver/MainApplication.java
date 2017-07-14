package com.noname.splitsaver;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.verify.client.VerifyClient;
import com.noname.splitsaver.Network.NetworkManager;

public class MainApplication extends Application {

    private static final String KEY_LOGGED_IN = "keyLoggedIn";
    private static VerifyClient verifyClient;

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

        NexmoClient nexmoClient;
        try {
            nexmoClient = new NexmoClient.NexmoClientBuilder()
                    .context(getApplicationContext())
                    .applicationId("674418ef-7dd1-4fdb-bf16-cc645b2eb9cf") //your App key
                    .sharedSecretKey("4d6547d61010730") //your App secret
                    .build();
            verifyClient = new VerifyClient(nexmoClient);
        } catch (ClientBuilderException e) {
            e.printStackTrace();
        }
    }

    public static VerifyClient getVerifyClient() {
        return verifyClient;
    }
}
