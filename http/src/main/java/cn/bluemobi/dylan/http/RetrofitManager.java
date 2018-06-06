package cn.bluemobi.dylan.http;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cn.bluemobi.dylan.http.ssl.Tls12SocketFactory;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Retrofit管理工具类
 * Created by yuandl on 2017-03-31.
 */

public class RetrofitManager {

    String TAG = "Http";
    private final int TIMEOUT = 15;
    private volatile static RetrofitManager singleton;
    private OkHttpClient mOkHttpClient;
    private Retrofit retrofit;
    private Object apiService;
    private Retrofit.Builder retrofitBuilder;

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

    private String secret;

    /**
     * 设置app加密签名秘钥
     *
     * @param secret app加密签名秘钥
     */
    public void setSecret(String secret) {
        this.secret = secret;
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
                    if (!TextUtils.isEmpty(appName) && !TextUtils.isEmpty(className) && !TextUtils.isEmpty(secret)) {
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
                mMessage.append("请求地址：");
                mMessage.append(original.url());
                mMessage.append("\n");

                mMessage.append("请求参数：");
                Request.Builder requestBuilder = original.newBuilder();
                addRequestParement(original);

                mMessage.append("\n");
                mMessage.append("请求大小：");
                mMessage.append(convertFileSize(original.body().contentLength()));

                Logger.d(mMessage.toString());

                Request request = requestBuilder.build();
                Response response = chain.proceed(request);

                mMessage.setLength(0);
                mMessage.append("响应地址：");
                mMessage.append(response.request().url());
                mMessage.append("\n");

                mMessage.append("响应参数：");
                addRequestParement(response.request());
                mMessage.append("\n");

                mMessage.append("响应耗时：");
                mMessage.append(formatDuring(response.receivedResponseAtMillis() - response.sentRequestAtMillis()));
                mMessage.append("\n");

                String content = response.body().string();
                okhttp3.MediaType mediaType = response.body().contentType();
                Response responseNew = response.newBuilder()
                        .body(ResponseBody.create(mediaType, content))
                        .build();
                mMessage.append("响应大小：");
                mMessage.append(convertFileSize(responseNew.body().contentLength()));
                mMessage.append("\n");

                mMessage.append("响应数据：");
                mMessage.append("\n");

                mMessage.append(JsonParse.formatJson(EncodeUtils.ascii2native(content)));

                Logger.d(mMessage.toString());

                return responseNew;
            }

            private void addRequestParement(Request original) {
                //请求体定制：统一添加sign参数
                if (original.body() instanceof FormBody) {
//                    FormBody.Builder newFormBody = new FormBody.Builder();
                    FormBody oidFormBody = (FormBody) original.body();
                    for (int i = 0; i < oidFormBody.size(); i++) {
                        String name = oidFormBody.encodedName(i);
                        String value = null;
                        try {
                            value = URLDecoder.decode(oidFormBody.encodedValue(i), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
//                        newFormBody.addEncoded(name, value);
                        if (mMessage.indexOf("=") != -1) {
                            mMessage.append("\n");
                            mMessage.append("　　　　　");
                        }
                        mMessage.append(name);
                        mMessage.append("=");
                        mMessage.append(value);
                    }
                } else if (original.body() instanceof MultipartBody) {
                    MultipartBody multipartBody = (MultipartBody) original.body();
                    for (MultipartBody.Part part : multipartBody.parts()) {
                        String name = getPartHeaders(part);
                        String value = null;
                        try {
                            value = URLDecoder.decode(getRequestBody(part).replaceAll("%(?![0-9a-fA-F]{2})", "%25"), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
//                        newFormBody.addEncoded(name, value);
                        if (mMessage.indexOf("=") != -1) {
                            mMessage.append("\n");
                            mMessage.append("　　　　　");
                        }
                        mMessage.append(name);
                        mMessage.append("=");
                        mMessage.append(value);
                    }
                }
            }
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置超时
        builder.readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES).writeTimeout(3, TimeUnit.MINUTES);

        /**
         * 添加其他自定义拦截器
         */
        for (Interceptor interceptor : interceptorList) {
            builder.addInterceptor(interceptor);
        }
        /**添加公共参数拦截器**/
        builder.addNetworkInterceptor(commParamsIntInterceptor);
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
        retrofitBuilder = new Retrofit.Builder();
        retrofit = retrofitBuilder
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
        this.apiService = retrofit.create(apiService);
    }

    private List<Interceptor> interceptorList = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor) {
        interceptorList.add(interceptor);
    }

    /**
     * 更换BaseUrl
     *
     * @param newBaseUrl
     */
    public void changeBaseUrl(String newBaseUrl) {
        retrofit = retrofitBuilder
                .baseUrl(newBaseUrl).build();
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
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        }};
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new SecureRandom());
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

    private String getPartHeaders(MultipartBody.Part part) {
        Class<?> personType = part.getClass();

        //访问私有属性
        Field field = null;
        try {
            field = personType.getDeclaredField("headers");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        field.setAccessible(true);
        try {
            Headers headers = (Headers) field.get(part);
            String s = headers.get("content-disposition");
            return s.split(";")[1].split("=")[1].replace("\"", "");

        } catch (IllegalAccessException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }

    private String getRequestBody(MultipartBody.Part part) {

        Class<?> personType = part.getClass();

        //访问私有属性
        Field field = null;
        try {
            field = personType.getDeclaredField("body");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        field.setAccessible(true);
        try {
            RequestBody requestBody = (RequestBody) field.get(part);
            MediaType contentType = requestBody.contentType();
            if (contentType.type().equals("multipart")) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset UTF8 = Charset.forName("UTF-8");
                Charset charset = contentType.charset(UTF8);
                return buffer.readString(charset);
            } else if (contentType.type().equals("image")) {
                return convertFileSize(requestBody.contentLength());
//                Class<?> requestBodyClass = requestBody.getClass();
//
//                //访问私有属性
//                Field fileRequestBodyClass = null;
//                try {
//                    fileRequestBodyClass = requestBodyClass.getDeclaredField("file");
//
//
//                    fileRequestBodyClass.setAccessible(true);
//                    File file = (File) fileRequestBodyClass.get(requestBody);
//                    Logger.d("file=" + file.getPath());
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}