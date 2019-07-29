package com.example.smsapp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RestfulApi {
    private static final String API_BASE_URL = "https://dev-cms.wallet.9pay.mobi";
    private static Retrofit retrofit;
    private PlfRestService restService;
    private static RestfulApi INSTANCE;

    public static RestfulApi getInstance(){
        if(INSTANCE == null){
            INSTANCE = new RestfulApi();
        }
        return INSTANCE;
    }
    private RestfulApi(){
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restService = retrofit.create(PlfRestService.class);
    }

    public Call<ResponseBody> postContent(String sender , String email , String emailBody , String messageBody , long time , String message) {
        return restService.postContent(sender, email,emailBody,messageBody, time , message);
    }
    public interface PlfRestService {
        @FormUrlEncoded
        @POST("/api/sms/message")
        Call<ResponseBody> postContent(
                @Field("phone_receive") String sender,
                @Field("email_from") String email,
                @Field("email_body") String emailBody,
                @Field("message_body") String messageBody,
                @Field("time") long time,
                @Field("content") String message
        );
    }
}
