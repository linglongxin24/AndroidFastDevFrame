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
     * 响应拦截器
     *
     * @param context 当前请求的上下文
     * @param status  状态码
     * @param msg     消息
     * @param data    数据
     * @return 如果返回true则停止往下执行，否则继续向下执行
     */
    boolean onResponse(Context context, int status, String msg, Map<String, Object> data);
}
