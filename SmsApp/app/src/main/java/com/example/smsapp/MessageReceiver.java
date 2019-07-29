package com.example.smsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
public class MessageReceiver  extends BroadcastReceiver {

    private static MessageListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0; i<pdus.length; i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String message = "Sender : " + smsMessage.getDisplayOriginatingAddress()
                    + "\nEmail From: " + smsMessage.getEmailFrom()
                    + "\nEmail Body: " + smsMessage.getEmailBody()
                    + "\nDisplay message body: " + smsMessage.getDisplayMessageBody()
                    + "\nTime in millisecond: " + smsMessage.getTimestampMillis()
                    + "\nMessage: " + smsMessage.getMessageBody();
            ContentSms contentSms = new ContentSms();
            contentSms.setSender(smsMessage.getDisplayOriginatingAddress());
            contentSms.setEmailFrom(smsMessage.getEmailFrom());
            contentSms.setEmailBody(smsMessage.getEmailBody());
            contentSms.setMessageBody(smsMessage.getDisplayMessageBody());
            contentSms.setTime(smsMessage.getTimestampMillis());
            contentSms.setMessage(smsMessage.getMessageBody());
            mListener.messageReceived(message , contentSms);
        }
    }

    public static void bindListener(MessageListener listener){
        mListener = listener;
    }
}
