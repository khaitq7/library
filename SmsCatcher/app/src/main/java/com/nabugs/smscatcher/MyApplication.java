package com.nabugs.smscatcher;

import android.app.Application;
import android.content.IntentFilter;
import android.provider.Telephony;
import android.util.Log;

import com.nabugs.smscatcher.broadcast.SmsBroadcastReceiver;

/**
 * Created by namanh on 6/1/2018.
 */

public class MyApplication extends Application {
    private static final String TAG = "DEV_JOB";
    private SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.d(TAG, "onCreate application: ");
//        smsBroadcastReceiver = new SmsBroadcastReceiver();
//        smsBroadcastReceiver.setListener(text -> {
//            Log.d(TAG, "onReceiveMessage: "+text);
//        });
//        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
    }

    @Override
    public void onTerminate() {
//        unregisterReceiver(smsBroadcastReceiver);
        super.onTerminate();
    }
}
