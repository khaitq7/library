package com.nabugs.smscatcher.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.nabugs.smscatcher.model.SmsMessage;

import java.util.List;

/**
 * Created by namanh on 6/4/2018.
 */

@Database(entities = {SmsMessage.class}, version = 1)
public abstract class SmsRoomDatabase extends RoomDatabase {
    public abstract SmsMessageDAO smsMessageDAO();
    private static SmsRoomDatabase INSTANCE;

    public static SmsRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (SmsRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SmsRoomDatabase.class, "sms_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

