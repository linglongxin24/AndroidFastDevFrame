package cn.bluemobi.dylan.http;

import android.content.Context;

import java.util.Map;

/**
 * 响应拦截器
 *
 * @author lenovo
 */
public interface ResponseInterceptor {
    /**
     * 请求异常
     *
     * @param e 异常信息
     */
    public void onError(Throwable e);

    /**
     * 响应拦截器
     *
     * @param context          当前请求的上下文
     * @param url              请求地址
     * @param requestParameter 请求参数
     * @param responseString   响应内容
     * @param httpResponseCode http响应码
     * @return 如果返回true则停止往下执行，否则继续向下执行
     */
    boolean onResponseStart(Context context, String url, Map<String, Object> requestParameter, String responseString, int httpResponseCode);

    /**
     * 响应拦截器
     *
     * @param context 当前请求的上下文
     * @param status  状态码
     * @param msg     消息
     * @param data    数据
     * @param url     请求地址
     * @return 如果返回true则停止往下执行，否则继续向下执行
     */
    boolean onResponse(Context context, int status, String msg, Map<String, Object> data, String url);
}
