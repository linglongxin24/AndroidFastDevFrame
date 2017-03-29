package cn.bluemobi.dylan.fastdev.http;

import java.util.Map;

/**
 * 网络请求响应
 * Created by yuandl on 2016/8/31 0031.
 */
public interface HttpResponse {
    /**
     * 开始访问网络
     */
    void netOnStart();

    void netOnStart(int requestCode);

    /**
     * 访问网络成功
     */
    void netOnSuccess(Map<String, Object> data, int requestCode);

    void netOnSuccess(Map<String, Object> data);

    /**
     * 访问网络成功获取原始数据
     */
    void netOnSuccessMetadata(String json, int requestCode);

    void netOnSuccessMetadata(String json);

    /**
     * 访问网络成功的其他状态
     */
    void netOnOtherStatus(int status, String msg, int requestCode);

    void netOnOtherStatus(int status, String msg);

    /**
     * 访问网络结束
     */
    void netOnFinish();

    void netOnFinish(int requestCode);

    /**
     * 访问网络失败
     */
    void netOnFailure(Throwable ex);

    void netOnFailure(int requestCode, Throwable ex);
}
