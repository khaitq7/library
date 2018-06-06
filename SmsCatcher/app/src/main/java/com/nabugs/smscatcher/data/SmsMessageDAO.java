package com.nabugs.smscatcher.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.nabugs.smscatcher.model.SmsMessage;

import java.util.List;

/**
 * Created by namanh on 6/4/2018.
 */

@Dao
public interface SmsMessageDAO {
    @Insert
    void insert(SmsMessage message);

    @Query("DELETE FROM sms_message")
    void deleteAll();

    @Delete
    void delete(SmsMessage sms);

    @Query("SELECT * FROM sms_message ORDER BY id DESC")
    LiveData<List<SmsMessage>> getAllSmsMessage();

    @Query("SELECT * FROM sms_message ORDER BY id DESC")
    List<SmsMessage> getAllSmsMessages();
}
