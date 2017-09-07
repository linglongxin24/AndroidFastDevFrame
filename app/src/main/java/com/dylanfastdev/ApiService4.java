package com.dylanfastdev;

import cn.bluemobi.dylan.http.download.ProgressResponseBody;
import okhttp3.ResponseBody;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yuandl on 2016-12-27.
 */

public interface ApiService4 {
//    String BASE_URL = "http://tingapi.ting.baidu.com/";
    String BASE_URL = "http://192.168.1.205:8081/campus/";

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
    @POST("app/getsysUserExamInfo")
    Observable<ResponseBody> getsysUserExamInfo();
}
