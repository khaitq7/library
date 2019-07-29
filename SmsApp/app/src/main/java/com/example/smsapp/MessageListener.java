package com.example.smsapp;

public interface MessageListener {
    void messageReceived(String message , ContentSms contentSms);
}
