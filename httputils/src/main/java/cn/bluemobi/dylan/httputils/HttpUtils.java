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
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cn.bluemobi.dylan.httputils.ssl.Tls12SocketFactory;
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

    private SSLSocketFactory factory;

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

    private String getCode() {
        return code;
    }

    private String getData() {
        return data;
    }

    private String getMsg() {
        return msg;
    }

    private int getSuccessCode() {
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


    /**
     * 默认使用中文
     */
    private static boolean useEnglishLanguage = false;

    /**
     * 默认debug模式打印日志
     */
    private boolean debugMode = true;

    /**
     * 设置是否启用日志
     *
     * @param debugMode
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * 设置提示中英文
     *
     * @param useEnglishLanguage
     */
    public void setUseEnglishLanguage(boolean useEnglishLanguage) {
        this.useEnglishLanguage = useEnglishLanguage;
    }

    /**
     * 默认在其他状态的时候给用户提醒响应的错误信息
     */
    private static MessageModel showMessageModel = MessageModel.NO;

    /**
     * 设置给用户提醒消息的模式
     *
     * @param messageModel
     */
    public void setShowMessageModel(MessageModel messageModel) {
        this.showMessageModel = messageModel;
    }

    /**
     * 用户提醒消息的模式
     */
    public enum MessageModel {
        All, OTHER_STATUS, NO
    }

    /**
     * 设置SSL证书
     *
     * @param certificates
     */
    public void setSSLSocketFactory(InputStream... certificates) {
        //载入证书
        factory = setCertificates(certificates);
    }

    /**
     * 默认忽略SSL证书
     */
    private boolean overlockCard = true;

    /**
     * 设置是否忽略证书验证
     *
     * @param overlockCard
     */
    public void setOverlockCard(boolean overlockCard) {
        this.overlockCard = overlockCard;
    }

    /**
     * 载入证书
     */
    private SSLSocketFactory setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(
                    null,
                    trustManagerFactory.getTrustManagers(),
                    new SecureRandom()
            );
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 忽略所有https证书
     */
    private SSLContext overlockCard() {
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        }};
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            return sslContext;
        } catch (Exception e) {
            Log.e(TAG, "ssl出现异常");
        }
        return null;

    }

    /**
     * 初始化mOkHttpClient与retrofit
     *
     * @param baseUrl 基础URL
     */
    private void initRetrofit(String baseUrl) {
        mOkHttpClient = new OkHttpClient();
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG, "OkHttp====message " + EncodeUtils.ascii2native(message));
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

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES).writeTimeout(3, TimeUnit.MINUTES); //设置超时
        if (debugMode) {
            /**添加打印日志拦截器**/
            builder.addInterceptor(logInterceptor);
        }
        /**添加公共参数拦截器**/
        builder.addInterceptor(commParamsIntInterceptor);
        /**设置证书**/
        if (overlockCard) {
            builder.sslSocketFactory(new Tls12SocketFactory(overlockCard().getSocketFactory()))
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
        } else {
            if (factory != null) {
                builder.sslSocketFactory(factory);
            }
        }

        mOkHttpClient = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
    }

    public static <T> T getApiService(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    public static Subscription post(final Context context, Observable<ResponseBody> mapObservable, final HttpResponse httpResponse) {
        return post(context, true, mapObservable, httpResponse);
    }

    /**
     * 要显示的对话框的上下文
     */
    private Context context;

    /**
     * 默认显示家在进度对话框
     */
    private boolean isShowLoadingDialog = true;
    /**
     * 接口对口
     */
    private Observable<ResponseBody> observable;

    /**
     * 【第一步】设置上下文
     *
     * @param context 上下文
     * @return 本类对象
     */
    public HttpUtils with(Context context) {
        this.context = context;
        return this;
    }

    /**
     * 【第二步】设置隐藏加载对话框（可忽略此步骤，默认显示）
     *
     * @return 本类对象
     */
    public HttpUtils hideLoadingDialog() {
        isShowLoadingDialog = false;
        return this;
    }

    /**
     * 【第三步】设置访问的几口接口
     *
     * @param observable 接口对象
     * @return 本类对象
     */
    public HttpUtils setObservable(Observable<ResponseBody> observable) {
        this.observable = observable;
        return this;
    }

    /**
     * 【第四步】设置访问接口的返回监听
     *
     * @param httpResponse 请求相应监听
     * @return 本类对象
     */
    public Subscription setDataListener(final HttpResponse httpResponse) {
        String network_unusual = useEnglishLanguage ? "Network  unusual" : "网络不可用";
        final String network_error = useEnglishLanguage ? "Network  error" : "网络繁忙";
        if (context == null) {
            throw new RuntimeException("请设置上下文对象--调用【with(Context context)】方法设置");
        }
        if (!NetworkUtil.isNetworkAvailable(context)) {
            Toast.makeText(context, network_unusual, Toast.LENGTH_SHORT).show();
            httpResponse.netOnFailure(new Exception(network_unusual));
            httpResponse.netOnFinish();
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
        if (observable == null) {
            throw new RuntimeException("请设置要调用的接口对象--调用【setObservable(Observable<ResponseBody> observable)】方法设置");
        }
        final LoadingDialog finalLoadingDialog = loadingDialog;
        Subscription subscribe = observable.subscribeOn(Schedulers.io())
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
                        context = null;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(context, network_error, Toast.LENGTH_SHORT).show();
                        httpResponse.netOnFailure(e);
                        onCompleted();

                    }

                    @Override
                    public void onNext(ResponseBody result) {
                        ArrayMap<String, Object> jsonBean;
                        try {
                            jsonBean = jsonParse(result.string());
                            String msg = getValue(jsonBean, HttpUtils.getInstance().getMsg());
                            int code = Integer.parseInt(getValue(jsonBean, HttpUtils.getInstance().getCode()));

                            if (showMessageModel == MessageModel.All) {
                                if (msg != null && !msg.isEmpty()) {
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (code == HttpUtils.getInstance().getSuccessCode()) {
                                Map<String, Object> data = (Map<String, Object>) jsonBean.get(HttpUtils.getInstance().getData());
                                httpResponse.netOnSuccess(data);
                            } else {
                                if (showMessageModel == MessageModel.OTHER_STATUS) {
                                    if (msg != null && !msg.isEmpty()) {
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                httpResponse.netOnOtherStatus(code, msg);
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, network_error, Toast.LENGTH_SHORT).show();
                            httpResponse.netOnFailure(e);
                            e.printStackTrace();
                        }
                    }
                });
        return subscribe;
    }

    public static Subscription post(final Context context, final boolean isShowLoadingDialog, Observable<ResponseBody> mapObservable, final HttpResponse httpResponse) {
        String network_unusual = useEnglishLanguage ? "Network  unusual" : "网络不可用";
        final String network_error = useEnglishLanguage ? "Network  error" : "网络繁忙";

        if (!NetworkUtil.isNetworkAvailable(context)) {
            Toast.makeText(context, network_unusual, Toast.LENGTH_SHORT).show();
            httpResponse.netOnFailure(new Exception(network_unusual));
            httpResponse.netOnFinish();
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
                        e.printStackTrace();
                        Toast.makeText(context, network_error, Toast.LENGTH_SHORT).show();
                        httpResponse.netOnFailure(e);
                        onCompleted();

                    }

                    @Override
                    public void onNext(ResponseBody result) {
                        ArrayMap<String, Object> jsonBean;
                        try {
                            jsonBean = jsonParse(result.string());
                            String msg = getValue(jsonBean, HttpUtils.getInstance().getMsg());
                            int code = Integer.parseInt(getValue(jsonBean, HttpUtils.getInstance().getCode()));

                            if (showMessageModel == MessageModel.All) {
                                if (msg != null && !msg.isEmpty()) {
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (code == HttpUtils.getInstance().getSuccessCode()) {
                                Map<String, Object> data = (Map<String, Object>) jsonBean.get(HttpUtils.getInstance().getData());
                                httpResponse.netOnSuccess(data);
                            } else {
                                if (showMessageModel == MessageModel.OTHER_STATUS) {
                                    if (msg != null && !msg.isEmpty()) {
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                httpResponse.netOnOtherStatus(code, msg);
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, network_error, Toast.LENGTH_SHORT).show();
                            httpResponse.netOnFailure(e);
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
