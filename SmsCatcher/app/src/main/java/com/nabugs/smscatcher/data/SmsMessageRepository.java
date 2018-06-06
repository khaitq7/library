package com.nabugs.smscatcher.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.nabugs.smscatcher.model.SmsMessage;

import java.util.List;

/**
 * Created by namanh on 6/4/2018.
 */

public class SmsMessageRepository {
    private SmsMessageDAO smsMessageDAO;
    private LiveData<List<SmsMessage>> allSmsMess;

    public SmsMessageRepository(Application application) {
        SmsRoomDatabase db = SmsRoomDatabase.getDatabase(application);
        smsMessageDAO = db.smsMessageDAO();
        allSmsMess = smsMessageDAO.getAllSmsMessage();
    }
    //==================================delete all message=====================================//
    public void delete(SmsMessage smsMessage) {
        new deleteAsyncTask(smsMessageDAO).execute(smsMessage);
    }

    private static class deleteAsyncTask extends AsyncTask<SmsMessage, Void, Void> {

        private SmsMessageDAO mAsyncTaskDao;

        deleteAsyncTask(SmsMessageDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SmsMessage... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    //====================delete all message=====================================//
    LiveData<List<SmsMessage>> getAllWords() {
        return allSmsMess;
    }

    //==================================delete all message=====================================//
    public void deleteAll() {
        new deleteAllAsyncTask(smsMessageDAO).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<SmsMessage, Void, Void> {

        private SmsMessageDAO mAsyncTaskDao;

        deleteAllAsyncTask(SmsMessageDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SmsMessage... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    //==================================insert message=====================================//
    public void insert(SmsMessage smsMessage) {
        new insertAsyncTask(smsMessageDAO).execute(smsMessage);
    }

    private static class insertAsyncTask extends AsyncTask<SmsMessage, Void, Void> {

        private SmsMessageDAO mAsyncTaskDao;

        insertAsyncTask(SmsMessageDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SmsMessage... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
