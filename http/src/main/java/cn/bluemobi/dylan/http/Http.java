package cn.bluemobi.dylan.http;

import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;

/**
 * 网络请求工具类
 * Created by yuandl on 2017-03-31.
 */

public class Http {
    private final String TAG = "HTTP";
    private static volatile Http http = null;
    private Integer loadingDialogLayoutId = null;

    private Http() {
    }

    public static Http getHttp() {
        if (http == null) {
            synchronized (Http.class) {
                if (http == null) {
                    http = new Http();
                }
            }
        }
        return http;
    }

    /**
     * 1.网络请求初始化工具类
     *
     * @param apiService  ApiService接口
     * @param baseUrl     基础URL
     * @param code        状态码字段
     * @param data        数据字段
     * @param msg         消息字段
     * @param successCode 成功的标识值
     * @param <T>         ApiService泛型
     * @return
     */
    public <T> Http init(Class<T> apiService, String baseUrl, String code, String data, String msg, int successCode) {
        //日志
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("HTTP")
                .build();
        //日志
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        RetrofitManager.getRetrofitManager().initRetrofit(apiService, baseUrl);
        JsonParse.getJsonParse().initJson(code, data, msg, successCode);
        return http;
    }

    /**
     * 2.设置提示中英文【可选】
     *
     * @param useEnglishLanguage
     */
    public Http setUseEnglishLanguage(boolean useEnglishLanguage) {
        MessageManager.getMessageManager().setUseEnglishLanguage(useEnglishLanguage);
        return http;
    }

    /**
     * 3.设置给用户提醒消息的模式【可选】
     *
     * @param messageModel
     */
    public Http setShowMessageModel(MessageManager.MessageModel messageModel) {
        MessageManager.getMessageManager().setShowMessageModel(messageModel);
        return http;
    }

    private boolean debugMode;

    /**
     * 4.设置是否启用日志【可选】
     *
     * @param debugMode
     */
    public Http setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        RetrofitManager.getRetrofitManager().setDebugMode(debugMode);
        return http;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    /**
     * 5.设置错误消息文字【可选】
     *
     * @param errorMessage
     */
    public Http setErrorMessage(String errorMessage) {
        MessageManager.getMessageManager().setErrorMessage(errorMessage);
        return http;
    }

    /**
     * 6.设置app签名秘钥
     *
     * @param secret 秘钥
     * @return
     */
    public Http setSecret(String secret) {
        RetrofitManager.getRetrofitManager().setSecret(secret);
        return this;
    }

    /**
     * 7.添加自定义拦截器
     *
     * @param interceptor 拦截器
     */

    public Http addInterceptor(Interceptor interceptor) {
        RetrofitManager.getRetrofitManager().addInterceptor(interceptor);
        return this;
    }

    /**
     * 8.设置默认超时时间
     *
     * @param timeout 时长
     * @param unit    单位
     * @return
     */
    public Http setDefaultTimeout(long timeout, TimeUnit unit) {
        RetrofitManager.getRetrofitManager().setDefaultTimeout(timeout, unit);
        return this;
    }

    /**
     * 获取ApiService
     *
     * @return
     */
    public static <T> T getApiService(Class<T> apiService) {
        return RetrofitManager.getRetrofitManager().getApiService(apiService);
    }

    /**
     * 更换BaseUrl
     *
     * @param newBaseUrl
     */
    public static void changeBaseUrl(String newBaseUrl) {
        RetrofitManager.getRetrofitManager().changeBaseUrl(newBaseUrl);
    }

    public void setLoadingDialog(int loadingDialogLayoutId) {
        this.loadingDialogLayoutId = loadingDialogLayoutId;
    }

    public Integer getLoadingDialogLayoutId() {
        return loadingDialogLayoutId;
    }

    /**
     * 开始调用接口
     *
     * @param context
     * @return
     */
    public static HttpRequest with(Context context) {
        HttpRequest httpRequest = new HttpRequest(context);
        if (responseInterceptor != null) {
            httpRequest.setResponseInterceptor(responseInterceptor);
        }
        return httpRequest;
    }

    private static ResponseInterceptor responseInterceptor;

    public void setResponseInterceptor(ResponseInterceptor responseInterceptor) {
        Http.responseInterceptor = responseInterceptor;
    }

}
