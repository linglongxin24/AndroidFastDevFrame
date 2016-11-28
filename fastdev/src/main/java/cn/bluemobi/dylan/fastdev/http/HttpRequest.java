package cn.bluemobi.dylan.fastdev.http;

import android.content.Context;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

/**
 * 发起网络请求
 * Created by yuandl on 2016/7/26 0026.
 */
public interface HttpRequest {
    /**
     * 同时只有一个请求
     *
     * @param context       请求的上下文
     * @param requestParams 请求的参数
     */
    Callback.Cancelable ajax(Context context, RequestParams requestParams, HttpResponse httpResponse);

    /**
     * 同时只有一个请求
     *
     * @param requestParams       请求的参数
     * @param isShowLoadingDialog 是否显示等待对话框
     */
    Callback.Cancelable ajax(Context context, RequestParams requestParams, boolean isShowLoadingDialog, HttpResponse httpResponse);

    /**
     * 同时有多个请求
     *
     * @param requestParams 请求的参数
     * @param requestCode   每次请求的请求码
     */
    Callback.Cancelable ajax(Context context, RequestParams requestParams, int requestCode, HttpResponse httpResponse);

    /**
     * 同时有多个请求
     *
     * @param requestParams       请求的参数
     * @param requestCode         每次请求的请求码
     * @param isShowLoadingDialog 是否显示等待对话框
     * @return Callback.Cancelable 回调是否可以取消
     */
    Callback.Cancelable ajax(Context context, RequestParams requestParams, int requestCode, boolean isShowLoadingDialog, HttpResponse httpResponse);
}
