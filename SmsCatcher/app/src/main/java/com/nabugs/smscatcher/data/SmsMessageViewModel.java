package com.nabugs.smscatcher.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.nabugs.smscatcher.model.SmsMessage;

import java.util.List;

/**
 * Created by namanh on 6/5/2018.
 */

public class SmsMessageViewModel extends AndroidViewModel{
    private SmsMessageRepository smsMessageRepository;
    private LiveData<List<SmsMessage>> listSmsMessages;

    public SmsMessageViewModel(@NonNull Application application) {
        super(application);
        smsMessageRepository = new SmsMessageRepository(application);
        listSmsMessages = smsMessageRepository.getAllWords();
    }

    public void insert(SmsMessage smsMessage){
        smsMessageRepository.insert(smsMessage);
    }

    public void deleteAll(){
        smsMessageRepository.deleteAll();
    }

    public LiveData<List<SmsMessage>> getListSmsMessages() {
        return listSmsMessages;
    }
}
