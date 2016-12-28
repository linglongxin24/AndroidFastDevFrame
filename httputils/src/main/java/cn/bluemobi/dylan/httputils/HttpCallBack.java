package cn.bluemobi.dylan.httputils;

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
    public void netOnFinish() {

    }

    @Override
    public void netOnFailure(Throwable ex) {

    }

}
