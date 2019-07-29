package com.example.smsapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestTask extends AsyncTask<String , Void , Void> {
    private static String TAG = RequestTask.class.getSimpleName();
    long time;
    public RequestTask(long  time){
        this.time = time;
    }
    @Override
    protected Void doInBackground(String... params) {
        RestfulApi restfulApi = RestfulApi.getInstance();
        String sender = params[0];
        String email = params[1];
        String emailBody = params[2];
        String messageBody = params[3];
        String message = params[4];
        Call<ResponseBody> call = restfulApi.postContent(sender , email , emailBody , messageBody , time , message);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                Log.d(TAG , "onSuccess");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG , "onFailure");
            }
        });
        return null;
    }
}
