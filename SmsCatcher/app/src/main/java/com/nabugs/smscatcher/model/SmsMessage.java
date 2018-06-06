package com.nabugs.smscatcher.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by namanh on 6/4/2018.
 */
@Entity(tableName = "sms_message")
public class SmsMessage {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String message;
    private String sender;

    public SmsMessage() {
    }

    public SmsMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
