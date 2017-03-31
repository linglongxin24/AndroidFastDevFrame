package com.dylanfastdev;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yuandl on 2016-12-27.
 */

public interface ApiService2 {
    String BASE_URL = "http://www.mmloveyou.com/app/";

    /**
     * 1.获取首页数据
     */
    @POST("index.php")
    Observable<ResponseBody> getHomeData();

    /**
     * 2.获取分类数据
     */
    @POST("category.php_src.php")
    Observable<ResponseBody> getClassifyData();
}
