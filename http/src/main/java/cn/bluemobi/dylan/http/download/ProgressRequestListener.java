package cn.bluemobi.dylan.http.download;

/**
 * 请求体进度回调接口，用于文件上传进度回调
 * Created by YDL on 2017/6/8.
 */

public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
