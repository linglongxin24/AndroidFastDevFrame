package com.dylanfastdev;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Created by yuandl on 2016-12-27.
 */

public interface ApiService {
    String baseUrl = "https://api.douban.com/v2/movie/";
//    @POST("top250")
//    Observable<Map<String, Object>> getTopMove(@Query("start") int start, @Query("count") int count);

    //    String baseUrl = "http://175.102.24.27:8912/api/";

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

    /**
     * 3.登录
     *
     * @param username uname
     * @param password password
     * @return Observable
     */
    @FormUrlEncoded
    @POST("http://192.168.1.178:8080/campus/app/appstu/login")
    Observable<Response<ResponseBody>> login(@Field("uname") String username,
                                   @Field("pwd") String password);

    /**
     * 3.测试
     *
     * @return Observable
     */
    @FormUrlEncoded
    @POST("http://192.168.1.222:8080/campus/app/activity/doList")
    Observable<Response<ResponseBody>> test(@Field("uid") String uid
            , @Field("pageSize") int pageSize
            , @Field("pageIndex") int pageIndex
    );
    /**
     * 3.测试
     *
     * @return Observable
     */
    @FormUrlEncoded
    @POST("https://www.travel-network.xin/mixin-apis/app/updateMemberInfo")
    Observable<Response<ResponseBody>> test2(
            @Field("nickName") String act,
            @Field("headPortrait") String pars,
            @Field("returnData") String returnData,
            @Field("accessToken") String accessToken
    );
    /**
     * 3.测试
     *
     * @return Observable
     */
    @POST("http://180.169.57.5:8088/api/V1/WAccount/CESHIForgetPasswordSetPWD")
    Observable<Response<ResponseBody>> restPassword(
            @Query("UserName") String UserName,
            @Query("Phone") String Phone,
            @Query("NewPwd") String NewPwd,
            @Query("YesPws") String YesPws
    );
    /**
     * 3.测试
     *
     * @return Observable
     */
    @POST("http://180.169.57.5:8088/api/V1/WAccount/CESHIForgetPasswordSetPWD")
    Observable<Response<ResponseBody>> restPassword(@Body RequestBody   requestBody);
    /**
     * 3.测试
     *
     * @return Observable
     */
    @Multipart
    @Streaming
    @POST("http://180.169.57.5:8088/api/V1/WAccount/PostFiles")
    Observable<Response<ResponseBody>> upload(@Part("Accountid")  RequestBody Accountid
                                               , @Part("AttachingType")  RequestBody AttachingType
                                               , @Part("Userid")  RequestBody Userid
                                               , @PartMap Map<String, RequestBody> file
    );
}
