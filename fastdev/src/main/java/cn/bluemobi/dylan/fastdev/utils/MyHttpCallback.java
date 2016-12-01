package cn.bluemobi.dylan.fastdev.utils;

import java.util.Map;

/**
 * Created by yuandl on 2016/6/27 0027.
 */
public interface MyHttpCallback {
    void netOnStart();

    void netOnSuccess(Map<String, Object> data, int requestCode);

    void netOnSuccess(Map<String, Object> data);

    void netOnOtherStatus(int status, String msg, int requestCode);
    void netOnOtherStatus(int status, String msg);

    void netOnFinish();

    void netOnFailure(Throwable ex);

}
