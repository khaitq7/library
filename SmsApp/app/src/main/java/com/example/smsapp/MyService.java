package com.example.smsapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyService extends Service implements MessageListener{
    private static final String TAG = MyService.class.getSimpleName();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO do something useful
        //smsHandler.sendEmptyMessageDelayed(DISPLAY_DATA, 1000);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MessageReceiver.bindListener(this);
    }

    @Override
    public void messageReceived(String message , ContentSms contentSms) {
        Log.d(TAG , "message : " + message);
        new RequestTask(contentSms.getTime()).execute(contentSms.getSender() , contentSms.getEmailFrom() , contentSms.getEmailBody() , contentSms.getMessageBody() , contentSms.getMessage());
    }

}
