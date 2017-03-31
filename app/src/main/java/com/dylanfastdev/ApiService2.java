package com.dylanfastdev;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yuandl on 2016-12-27.
 */

public interface ApiService {
//    String baseUrl = "https://api.douban.com/v2/movie/";
//    @POST("top250")
//    Observable<Map<String, Object>> getTopMove(@Query("start") int start, @Query("count") int count);

    String baseUrl = "http://175.102.24.27:8912/api/";

    String secret = "O]dWJ,[*g)%k\"?q~g6Co!`cQvV>>Ilvw";

    @FormUrlEncoded
    @POST(" ")
    Observable<ResponseBody> getTopMove(@Field("app") String app, @Field("class") String className);
}
