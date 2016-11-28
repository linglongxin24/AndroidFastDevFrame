package cn.bluemobi.dylan.fastdev.http;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bluemobi.dylan.fastdev.utils.CheckNetwork;
import cn.bluemobi.dylan.fastdev.view.LoadingDialog;

/**
 * 网络请求工具类
 * Created by yuandl on 2016/8/31 0031.
 */
public class HttpUtils implements HttpRequest {
    private String code = "code";
    private String msg = "msg";
    private String data = "data";
    private int successCode = 1;
    private Map<String, String> globalParameters = new ArrayMap<>();


    //    private String prams = "tableCode";
//    private String value = "0001";
    private volatile static HttpUtils httpUtils;


    private HttpUtils() {
    }

    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                if (httpUtils == null) {
                    httpUtils = new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    @Override
    public Callback.Cancelable ajax(Context context, RequestParams requestParams, HttpResponse httpResponse) {
        return ajax(context, requestParams, -1, true, httpResponse);
    }

    @Override
    public Callback.Cancelable ajax(Context context, RequestParams requestParams, boolean isShowLoadingDialog, HttpResponse httpResponse) {
        return ajax(context, requestParams, -1, isShowLoadingDialog, httpResponse);
    }

    @Override
    public Callback.Cancelable ajax(Context context, RequestParams requestParams, int requestCode, HttpResponse httpResponse) {
        return ajax(context, requestParams, -1, true, httpResponse);
    }

    @Override
    public Callback.Cancelable ajax(Context context, RequestParams requestParams, int requestCode, boolean isShowLoadingDialog, HttpResponse httpResponse) {
        if (!CheckNetwork.isNetworkAvailable(context)) {
            Toast.makeText(context, "网络不可用，请检查网络连接！", Toast.LENGTH_SHORT).show();
            httpResponse.netOnFailure(requestCode, new Exception("网络不可用"));
            return null;
        }
        LoadingDialog loadingDialog = null;
        if (isShowLoadingDialog) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(context);
            }
        } else {
            loadingDialog = null;
        }
        Set<String> set = globalParameters.keySet();
        for (String key : set) {
            requestParams.addBodyParameter(key, globalParameters.get(key));
        }
        List<KeyValue> params = requestParams.getStringParams();
        String requestParamstr = "url=" + requestParams.getUri();
        for (KeyValue keyValue : params) {
            if (keyValue.key.contains(":")) {
                throw new RuntimeException("参数异常！");
            }
            requestParamstr += "\n" + keyValue.key + "=" + keyValue.getValueStr();
        }

        Logger.d(requestParamstr);
        HttpCallBack httpCallBack = new HttpCallBack(context, requestCode, httpResponse, loadingDialog);
        Callback.Cancelable cancelable = x.http().post(requestParams, httpCallBack);
        return cancelable;
    }

    public void init(String code, String data, String msg, int successCode) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.successCode = successCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(int successCode) {
        this.successCode = successCode;
    }

    public Map<String, String> getGlobalParameters() {
        return globalParameters;
    }

    public void setGlobalParameters(Map<String, String> globalParameters) {
        this.globalParameters = globalParameters;
    }
}
