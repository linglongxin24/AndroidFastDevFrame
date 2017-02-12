package cn.bluemobi.dylan.fastdev.http;

import java.util.Map;

/**网络请求响应
 * Created by yuandl on 2016/8/31 0031.
 */
public  abstract class CommCallBack implements HttpResponse{


    @Override
    public void netOnStart() {

    }

    @Override
    public void netOnStart(int requestCode) {

    }

    @Override
    public void netOnSuccess(Map<String, Object> data, int requestCode) {

    }
    public abstract   void netOnSuccess(Map<String, Object> data);


    @Override
    public void netOnOtherStatus(int status, String msg, int requestCode) {

    }

    @Override
    public void netOnOtherStatus(int status, String msg) {

    }

    @Override
    public void netOnFinish() {

    }

    @Override
    public void netOnFinish(int requestCode) {

    }

    @Override
    public void netOnFailure(Throwable ex) {

    }

    @Override
    public void netOnFailure(int requestCode, Throwable ex) {

    }
}
