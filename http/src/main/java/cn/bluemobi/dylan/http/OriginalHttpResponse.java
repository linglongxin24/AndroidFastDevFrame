package cn.bluemobi.dylan.http;

/**
 * @author dylan
 * @date 2019-01-18
 */
public interface OriginalHttpResponse {

    /**
     * 开始访问网络
     */
    void netOnStart();

    /**
     * 访问网络成功
     */
    void netOnSuccess(int httpCode,String json);

    /**
     * 访问网络结束
     */
    void netOnFinish();

    /**
     * 访问网络失败
     */
    void netOnFailure(Throwable ex);
}
