package com.dylanfastdev;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by yuandl on 2016-12-27.
 */

public interface ApiService {
//    String baseUrl = "https://api.douban.com/v2/movie/";
//    @POST("top250")
//    Observable<Map<String, Object>> getTopMove(@Query("start") int start, @Query("count") int count);

    //    String baseUrl = "http://175.102.24.27:8912/api/";
    String baseUrl = "http://workstation.365wanmeng.com:8019/api/";

    String secret = "O]dWJ,[*g)%k\"?q~g6Co!`cQvV>>Ilvw";

    @FormUrlEncoded
    @POST(" ")
    Observable<ResponseBody> getTopMove(@Field("app") String app, @Field("class") String className);

    /**
     * @param app              Task
     * @param className        Task_an_AddNeed
     * @param sign             13b30123e894904eafe2c4a9a7d64bea
     * @param sid              46
     * @param title            1
     * @param content          2
     * @param attachment_count 1
     * @param attachment0      /storage/emulated/0/PictureSelector/CameraImage/PictureSelector_20170823_130257.mp4
     * @param attachment0      /storage/emulated/0/PictureSelector/CameraImage/PictureSelector_20170823_130257.mp4
     * @return
     */
    @Multipart
    @POST(" ")
    Observable<ResponseBody> upload(@Part("app") RequestBody app
            , @Part("class") RequestBody className
            , @Part("sign") RequestBody sign
            , @Part("sid") RequestBody sid
            , @Part("title") RequestBody title
            , @Part("content") RequestBody content
            , @Part("attachment_count") RequestBody attachment_count
            , @Part("attachment0") RequestBody attachment
    );
}
