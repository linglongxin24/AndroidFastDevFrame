package cn.bluemobi.dylan.httputils.http;

import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.orhanobut.logger.Logger;

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
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Retrofit管理工具类
 * Created by yuandl on 2017-03-31.
 */

public class RetrofitManager {

    String TAG = "Http";
    private static final int TIMEOUT = 15;
    private volatile static RetrofitManager singleton;
    private OkHttpClient mOkHttpClient;
    private static Retrofit retrofit;
    private static Object apiService;

    private RetrofitManager() {
    }

    public static RetrofitManager getRetrofitManager() {
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


    public <T> void initRetrofit(Class<T> apiService, String baseUrl) {
        mOkHttpClient = new OkHttpClient();

        /**
         * 公共参数拦截器
         */
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
        /**
         * http请求日志拦截器
         */
        Interceptor httpInterceptor = new Interceptor() {

            private StringBuilder mMessage = new StringBuilder();

            @Override
            public Response intercept(Chain chain) throws IOException {
                mMessage.setLength(0);
                Request original = chain.request();
                mMessage.append("请求地址：" + original.url());
                mMessage.append("\n");
                mMessage.append("请求体大小：" + original.body().contentLength());
                mMessage.append("\n");
                mMessage.append("请求参数：");
                Request.Builder requestBuilder = original.newBuilder();
                //请求体定制：统一添加sign参数
                if (original.body() instanceof FormBody) {
                    FormBody.Builder newFormBody = new FormBody.Builder();
                    FormBody oidFormBody = (FormBody) original.body();
                    for (int i = 0; i < oidFormBody.size(); i++) {
                        String name = oidFormBody.encodedName(i);
                        String value = oidFormBody.encodedValue(i);
                        newFormBody.addEncoded(name, value);
                        mMessage.append(mMessage.indexOf("=") != -1 ? "&" : "");
                        mMessage.append(name + "=" + value);
                    }
                }
                Logger.d(mMessage.toString());

                Request request = requestBuilder.build();
                Response response = chain.proceed(request);

                mMessage.setLength(0);
                mMessage.append("响应地址：" + response.request().url());
                mMessage.append("\n");

                mMessage.append("响应参数：");
                if (response.request().body() instanceof FormBody) {
                    FormBody oidFormBody = (FormBody) response.request().body();
                    for (int i = 0; i < oidFormBody.size(); i++) {
                        String name = oidFormBody.encodedName(i);
                        String value = oidFormBody.encodedValue(i);
                        mMessage.append(mMessage.indexOf("=") != -1 ? "&" : "");
                        mMessage.append(name + "=" + value);
                    }
                }
                mMessage.append("\n");

                mMessage.append("响应耗时：" + formatDuring(response.receivedResponseAtMillis() - response.sentRequestAtMillis()));
                mMessage.append("\n");

                String content = response.body().string();
                okhttp3.MediaType mediaType = response.body().contentType();
                Response responseNew = response.newBuilder()
                        .body(ResponseBody.create(mediaType, content))
                        .build();
                mMessage.append("响应大小：" + convertFileSize(responseNew.body().contentLength()));
                mMessage.append("\n");

                mMessage.append("响应数据：");
                mMessage.append("\n");

                mMessage.append("" + JsonParse.formatJson(EncodeUtils.ascii2native(content)));

                Logger.d(mMessage.toString());

                return responseNew;
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
            builder.addInterceptor(httpInterceptor);
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
        this.apiService = retrofit.create(apiService);
    }

    /**
     * 获取ApiService
     *
     * @param apiService
     * @param <T>
     * @return
     */
    public <T> T getApiService(Class<T> apiService) {
        return retrofit.create(apiService);
    }

    /**
     * 获取ApiService
     *
     * @param <T>
     * @return
     */
    public <T> T getApiService() {
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

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    /**
     * @param mss 要转换的毫秒数
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {
        if (mss < 1000) {
            return mss + "ms";
        } else if (mss < 1000 * 60) {
            return (mss % (1000 * 60)) / 1000 + "s";
        } else {
            return (mss % (1000 * 60 * 60)) / (1000 * 60) + "min";
        }
//        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
//        long seconds = (mss % (1000 * 60)) / 1000;
//        return minutes + " minutes "
//                + seconds + " seconds ";
    }

}