package cn.bluemobi.dylan.http;

import java.util.Map;

/**
 * 网络请求回调
 * Created by yuandl on 2016/8/31 0031.
 */
public abstract class HttpCallBack implements HttpResponse {


    @Override
    public void netOnStart() {
    }

    @Override
    public void netOnOtherStatus(int status, String msg) {

    }

    @Override
    public void netOnSuccessServerError(int code, String errorMessage) {

    }

    @Override
    public void netOnFinish() {

    }

    @Override
    public void netOnFailure(Throwable ex) {

    }

    @Override
    public void onResponseProgress(long bytesRead, long contentLength, boolean done) {

    }

    @Override
    public void netOnSuccess(Map<String, Object> data, String msg) {

    }

    @Override
    public void netOnOtherStatus(int status, String msg, Map<String, Object> data) {

    }
}
