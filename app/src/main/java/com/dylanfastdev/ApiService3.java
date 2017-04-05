package com.dylanfastdev;

import android.support.annotation.NonNull;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 接口
 * Created by dylan on 2017-03-28.
 */

public interface ApiService3 {
    String BASE_URL = "http://115.28.228.110:8080/shucai/";

    /**
     * 1.获取首页轮播图
     *
     * @return
     */
    @POST("InfoAdvertise_findInfoAdvertise.action")
    Observable<ResponseBody> getAD();


    /**
     * 2.获取短信验证码
     *
     * @param username 手机号
     * @return
     */
    @FormUrlEncoded
    @POST("InfoUser_getSmsCode.action")
    Observable<ResponseBody> getCode(@Field("username") String username);

    /**
     * 3.注册
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("InfoUser_registerUser.action")
    Observable<ResponseBody> register(@Field("username") String username, @Field("password") String password);

    /**
     * 4.登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("InfoUser_login.action")
    Observable<ResponseBody> login(@Field("username") String username, @Field("password") String password);

    /**
     * 5 找回密码
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("InfoUser_findPassWord.action")
    Observable<ResponseBody> forgetPassword(@Field("username") String username, @Field("password") String password);

    /**
     * 6 获取商品列表
     *
     * @param type           商品类型（ 1：单品，2：成品，3：套餐） 【必传】
     * @param name           商品名称
     * @param inforawtype    单品（原料）类型ID
     * @param finishtype     成品菜系类型ID
     * @param finishhealth   成品健康类型ID
     * @param taocannum      套餐中包含的成品数量
     * @param ishotsale      是否热卖榜 0：否，1：是
     * @param isbargainprice 是否特价 0：否，1：是
     * @param isrecommend    是否店主推荐 0：否，1：是
     * @param isnetshare     是否网友分红 0：否，1：是
     * @param pageIndex      当前页面的页数 【必传】
     * @param pageSize       页面大小 【必传】
     * @return
     */
    @FormUrlEncoded
    @POST("InfoGoods_findGoods.action")
    Observable<ResponseBody> getGoodsList(@NonNull @Field("type ") String type,
                                          @Field("name") String name,
                                          @Field("inforawtype.id") String inforawtype,
                                          @Field("finishtype.id") String finishtype,
                                          @Field("finishhealth.id") String finishhealth,
                                          @Field("taocannum ") String taocannum,
                                          @Field("ishotsale") String ishotsale,
                                          @Field("isbargainprice") String isbargainprice,
                                          @Field("isrecommend") String isrecommend,
                                          @Field("isnetshare") String isnetshare,
                                          @NonNull @Field("pageIndex") int pageIndex,
                                          @NonNull @Field("pageSize") int pageSize
    );
    /**
     * 24,修改用户信息
     *
     * @param userId      用户ID 【必须】
     * @param anothername 昵称
     * @param password    用户密码【前端加密之后】
     * @param sex         0：女，1：男，2：保密
     * @param imageHead   imageHead
     * @return
     */
    @Multipart
    @POST("InfoUser_modifyUser.action")
    Observable<ResponseBody> editInfo(@Part("id") RequestBody userId,
                                      @Part("anothername") RequestBody anothername,
                                      @Part("password") RequestBody password,
                                      @Part("sex") RequestBody sex,
                                      @Part MultipartBody.Part imageHead
    );
}
