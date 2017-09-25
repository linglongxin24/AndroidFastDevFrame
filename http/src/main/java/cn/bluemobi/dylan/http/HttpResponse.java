package cn.bluemobi.dylan.http;

import java.util.Map;

import cn.bluemobi.dylan.http.download.ProgressResponseListener;

/**
 * 网络请求响应
 * Created by yuandl on 2016/8/31 0031.
 */
public interface HttpResponse extends ProgressResponseListener {
    /**
     * 开始访问网络
     */
    void netOnStart();

    /**
     * 访问网络成功
     */
    void netOnSuccess(Map<String, Object> data);

    /**
     * 访问网络成功,带msg
     */
    void netOnSuccess(Map<String, Object> data, String msg);

    /**
     * 访问网络成功的其他状态
     */
    void netOnOtherStatus(int status, String msg);

    /**
     * 访问网络成功的其他状态,带数据
     */
    void netOnOtherStatus(int status, String msg, Map<String, Object> data);

    /**
     * 访问网络结束
     */
    void netOnFinish();

    /**
     * 访问网络失败
     */
    void netOnFailure(Throwable ex);
}
