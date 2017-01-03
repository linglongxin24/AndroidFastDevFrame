package cn.bluemobi.dylan.httputils;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Http请求工具类
 * Created by yuandl on 2016-12-27.
 */

public class HttpUtils {
    String TAG = "HttpUtils";
    /**
     * 单例
     */
    private volatile static HttpUtils httpUtils;
    private OkHttpClient mOkHttpClient;
    private static Retrofit retrofit;

    /**
     * 构造函数私有化
     */
    private HttpUtils() {
    }

    /**
     * 获取网络请求工具类单例
     *
     * @return
     */
    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                if (httpUtils == null) {
                    httpUtils = new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    /**
     * 请求接口返回码
     */
    private String code = "code";
    /**
     * 请求接口返回信息
     */
    private String msg = "msg";
    /**
     * 请求接口返回数据
     */
    private String data = "data";
    /**
     * 请求接口成功的响应码
     */
    private int successCode = 1;
    /**
     * 配置请求接口的全局参数
     */
    private Map<String, String> globalParameters = new ArrayMap<>();

    public String getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public int getSuccessCode() {
        return successCode;
    }


    /**
     * 初始化各种参数
     *
     * @param code             请求接口返回码
     * @param data             请求接口返回数据
     * @param msg              请求接口返回信息
     * @param successCode      请求接口成功的响应码
     * @param globalParameters 配置请求接口的全局参数
     */
    public void init(String baseUrl, String code, String data, String msg, int successCode, Map<String, String> globalParameters) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.successCode = successCode;
        this.globalParameters = globalParameters;
        initRetrofit(baseUrl);
    }

    private void initRetrofit(String baseUrl) {
        mOkHttpClient = new OkHttpClient();
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG, "OkHttp====message " + EncodeUtils.convertUnicode(message));
            }

        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor commParamsIntInterceptor = new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                //请求体定制：统一添加sign参数
                if (original.body() instanceof FormBody) {
                    FormBody.Builder newFormBody = new FormBody.Builder();
                    FormBody oidFormBody = (FormBody) original.body();
                    String appName = "";
                    String className = "";
                    for (int i = 0; i < oidFormBody.size(); i++) {
                        String name = oidFormBody.encodedName(i);
                        String value = oidFormBody.encodedValue(i);
                        if ("app".equals(name)) {
                            appName = value;
                        } else if ("class".equals(name)) {
                            className = value;
                        }
                        newFormBody.addEncoded(name, value);
                    }
                    String secret = "O]dWJ,[*g)%k\"?q~g6Co!`cQvV>>Ilvw";
                    newFormBody.add("sign", MD5Utils.md5(appName + className + secret));
                    requestBuilder.method(original.method(), newFormBody.build());
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .addInterceptor(commParamsIntInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
    }

    public static <T> T getApiService(Class<T> clazz) {
        return retrofit.create(clazz);
    }
//
//    private static LoadingDialog loadingDialog;

    public static Subscription post(final Context context, Observable<ResponseBody> mapObservable, final HttpResponse httpResponse) {
        return post(context, true, mapObservable, httpResponse);
    }

    public static Subscription post(final Context context, final boolean isShowLoadingDialog, Observable<ResponseBody> mapObservable, final HttpResponse httpResponse) {

        if (!NetworkUtil.isNetworkAvailable(context)) {
            Toast.makeText(context, "Network  unusual", Toast.LENGTH_SHORT).show();
            httpResponse.netOnFailure(new Exception("网络不可用"));
            return null;
        }

        LoadingDialog loadingDialog = null;
        if (isShowLoadingDialog) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(context);
            }
        } else {
            loadingDialog = null;
        }

        final LoadingDialog finalLoadingDialog = loadingDialog;
        Subscription subscribe = mapObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {

                    @Override
                    public void onStart() {
                        if (finalLoadingDialog != null) {
                            finalLoadingDialog.show("");
                        }
                        httpResponse.netOnStart();
                    }

                    @Override
                    public void onCompleted() {
                        if (finalLoadingDialog != null) {
                            finalLoadingDialog.dismiss();
                        }

                        httpResponse.netOnFinish();

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (finalLoadingDialog != null) {
                            finalLoadingDialog.dismiss();
                        }

                        e.printStackTrace();
                        Toast.makeText(context, "Network  error", Toast.LENGTH_SHORT).show();
                        httpResponse.netOnFailure(e);


                    }

                    @Override
                    public void onNext(ResponseBody result) {
                        ArrayMap<String, Object> jsonBean;
                        try {
                            jsonBean = jsonParse(result.string());
                            String msg = getValue(jsonBean, HttpUtils.getInstance().getMsg());
                            int code = Integer.parseInt(getValue(jsonBean, HttpUtils.getInstance().getCode()));

//                            if (msg != null && !msg.isEmpty()) {
//                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//                            }
                            if (code == HttpUtils.getInstance().getSuccessCode()) {
                                Map<String, Object> data = (Map<String, Object>) jsonBean.get(HttpUtils.getInstance().getData());
                                httpResponse.netOnSuccess(data);
                            } else {
                                httpResponse.netOnOtherStatus(code, msg);
                            }
                        } catch (Exception e) {
                            onError(e);
                            e.printStackTrace();
                        }
                    }
                });
        return subscribe;
    }

    private static ArrayMap<String, Object> jsonParse(String json) throws JSONException {
        ArrayMap<String, Object> arrayMap = JSON.parseObject(json, new TypeReference<ArrayMap<String, Object>>() {
        }.getType());
        ArrayMap<String, Object> returnData = new ArrayMap<String, Object>();
        ArrayMap<String, Object> rrData = null;
        String dataStrKey = HttpUtils.getInstance().getData();
        if (arrayMap.containsKey(dataStrKey)) {
            Object data = arrayMap.get(dataStrKey);
            if (data instanceof String) {
                rrData = new ArrayMap<String, Object>();
                rrData.put(dataStrKey, data.toString());
                returnData.put(dataStrKey, rrData);
            } else if (data instanceof JSONArray) {
                rrData = new ArrayMap<String, Object>();
                rrData.put(dataStrKey, data);
                returnData.put(dataStrKey, rrData);
            } else if (data instanceof com.alibaba.fastjson.JSONObject) {
                rrData = JSON.parseObject(data.toString(), new TypeReference<ArrayMap<String, Object>>() {
                }.getType());
                returnData.put(dataStrKey, rrData);
            } else {
                returnData.put(dataStrKey, new ArrayMap<>());
            }
        } else {
            rrData = new ArrayMap<>();
            Set<String> keys2 = arrayMap.keySet();
            for (String s : keys2) {
                if (!s.equals(dataStrKey)) {
                    rrData.put(s, arrayMap.get(s));
                }
            }
            returnData.put(dataStrKey, rrData);
        }
        returnData.put(HttpUtils.getInstance().getCode(), getValue(arrayMap, HttpUtils.getInstance().getCode()));
        returnData.put(HttpUtils.getInstance().getMsg(), getValue(arrayMap, HttpUtils.getInstance().getMsg()));
        return returnData;
    }

    /**
     * 获取map中的值
     *
     * @param map map
     * @param key map的key
     * @return map的值
     */
    public static String getValue(Map<String, Object> map, String key) {
        if (map == null || map.size() == 0) {
            return "";
        } else if (isNull(key)) {
            return "";
        } else if (map.containsKey(key)) {
            Object data = map.get(key);
            if (data instanceof String) {
                if (isNull2((String) map.get(key))) {
                    return "";
                } else {
                    return map.get(key).toString();
                }
            } else {
                return String.valueOf(map.get(key));
            }

        } else {
            return "";
        }
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(String s) {
        return null == s || s.equals("") || s.equalsIgnoreCase("null");

    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull2(String s) {
        return null == s || s.equals("");

    }
}
