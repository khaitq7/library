package com.nabugs.smscatcher.broadcast;

/**
 * Created by namanh on 6/1/2018.
 */

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.nabugs.smscatcher.data.SmsMessageViewModel;
import com.nabugs.smscatcher.data.SmsRoomDatabase;

/**
 * A broadcast receiver who listens for incoming SMS
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public SmsBroadcastReceiver(){
//        serviceProviderNumber = "11111";
//        serviceProviderSmsCondition = "101010101";
    }

    private static final String TAG = "SmsBroadcastReceiver";

//    private final String serviceProviderNumber;
//    private final String serviceProviderSmsCondition;

    private Listener listener;

//    public SmsBroadcastReceiver(String serviceProviderNumber, String serviceProviderSmsCondition) {
//        this.serviceProviderNumber = serviceProviderNumber;
//        this.serviceProviderSmsCondition = serviceProviderSmsCondition;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String smsSender = "";
            String smsBody = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsSender = smsMessage.getDisplayOriginatingAddress();
                    smsBody += smsMessage.getMessageBody();
                }
            } else {
                Bundle smsBundle = intent.getExtras();
                if (smsBundle != null) {
                    Object[] pdus = (Object[]) smsBundle.get("pdus");
                    if (pdus == null) {
                        // Display some error to the user
                        Log.e(TAG, "SmsBundle had no pdus key");
                        return;
                    }
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        smsBody += messages[i].getMessageBody();
                    }
                    smsSender = messages[0].getOriginatingAddress();
                }
            }

//            if (smsSender.equals(serviceProviderNumber) && smsBody.startsWith(serviceProviderSmsCondition)) {
                if (listener != null) {
                    listener.onTextReceived(smsBody);
                }
//            }

            com.nabugs.smscatcher.model.SmsMessage smsMess = new com.nabugs.smscatcher.model.SmsMessage(smsBody, smsSender);
            SmsMessageViewModel smsMessageViewModel = new SmsMessageViewModel((Application) context.getApplicationContext());
            smsMessageViewModel.insert(smsMess);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onTextReceived(String text);
    }
}
