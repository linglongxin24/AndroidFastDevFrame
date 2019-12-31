package com.dylanfastdev;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yuandl on 2016-12-27.
 */

public interface ApiService4 {
    //    String BASE_URL = "http://tingapi.ting.baidu.com/";

    /**
     * 1.获取首页数据
     */
    @POST("v1/restserver/ting?method=baidu.ting.song.lry&songid=1137385")
    Observable<ResponseBody> getHomeData();

    /**
     * 2.获取分类数据
     */
    @POST("category.php_src.php")
    Observable<ResponseBody> getClassifyData();

    /**
     * 2.获取分类数据
     */
    @FormUrlEncoded
    @POST("app/getsysUserExamInfo")
    Observable<ResponseBody> getsysUserExamInfo(@Field("p") String p);
    /**
     * 94 运动会-获取竞赛列表
     *
     * @param competitionId 竞赛ID,有竞赛ID，则说明该竞赛存在子竞赛
     * @param pageIndex     页码
     * @param pageSize      每页大小
     * @return Observable
     */

    @POST("app/appCompetition/getAppCompetitionList")
    @FormUrlEncoded
    Observable<ResponseBody> getCompetitionList(@Field("competitionId") String competitionId,
                                                @Field("pageIndex") int pageIndex,
                                                @Field("pageSize") int pageSize);
    /**
     * 6.添加提交跑步记录
     *
     * @return Observable
     */
    @FormUrlEncoded
    @POST("http://192.168.1.175:8080/campus/app/appSportRecord/appAddSportRecord")
    Observable<Response<ResponseBody>> addRunRecorder(@Field("userId") String userId,
                                                      @Field("runType") int runType,
                                                      @Field("startTime") long startTime,
                                                      @Field("endTime") long endTime,
                                                      @Field("gitudeLatitude") String latLngList,
                                                      @Field("identify") String identify,
                                                      @Field("formatSportTime") String formatSportTime,
                                                      @Field("formatSportRange") String formatSportRange,
                                                      @Field("avgspeed") String avgSpeed,
                                                      @Field("speed") String speed,
                                                      @Field("okPointList") String okPointList,
                                                      @Field("brand") String brand,
                                                      @Field("model") String model,
                                                      @Field("system") String system,
                                                      @Field("version") String version,
                                                      @Field("appVersion") String appVersion,
                                                      @Field("stepNumbers") String stepNumbers,
                                                      @Field("isFaceStatus") String isFaceStatus
    );
}
