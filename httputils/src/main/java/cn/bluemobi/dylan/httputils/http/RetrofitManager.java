package cn.bluemobi.dylan.httputils.http;

import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cn.bluemobi.dylan.httputils.EncodeUtils;
import cn.bluemobi.dylan.httputils.MD5Utils;
import cn.bluemobi.dylan.httputils.ssl.Tls12SocketFactory;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Retrofit管理工具类
 * Created by yuandl on 2017-03-31.
 */

public class RetrofitManager {

    String TAG = "HttpUtils";
    private static final int TIMEOUT = 15;
    private volatile static RetrofitManager singleton;
    private OkHttpClient mOkHttpClient;
    private static Retrofit retrofit;
    private static Object apiService;

    private RetrofitManager() {
    }

    public static RetrofitManager getInstance() {
        if (singleton == null) {
            synchronized (RetrofitManager.class) {
                if (singleton == null) {
                    singleton = new RetrofitManager();
                }
            }
        }
        return singleton;
    }

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


    public <T> void initRetrofit(Class<T> clazz, String baseUrl) {
        mOkHttpClient = new OkHttpClient();
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
                    if (!TextUtils.isEmpty(appName) && !TextUtils.isEmpty(className)) {
                        String secret = "O]dWJ,[*g)%k\"?q~g6Co!`cQvV>>Ilvw";
                        newFormBody.add("sign", MD5Utils.md5(appName + className + secret));
                        requestBuilder.method(original.method(), newFormBody.build());
                    }
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG, "OkHttp====message " + EncodeUtils.ascii2native(message));
            }

        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES).writeTimeout(3, TimeUnit.MINUTES); //设置超时
        /**添加公共参数拦截器**/
        builder.addInterceptor(commParamsIntInterceptor);

        if (debugMode) {
            /**添加打印日志拦截器**/
            builder.addInterceptor(logInterceptor);
        }
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
        apiService = retrofit.create(clazz);
    }

    /**
     * 获取ApiService
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getApiService(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    /**
     * 获取ApiService
     *
     * @param <T>
     * @return
     */
    public static <T> T getApiService() {
        return (T) apiService;
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
     * 证书
     */
    private SSLSocketFactory factory;

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
}