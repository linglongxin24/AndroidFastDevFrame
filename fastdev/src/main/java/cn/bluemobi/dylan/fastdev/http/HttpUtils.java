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
    /**
     * 请求接口返回码
     */
    private String code = "code";
    /**
     * 请求接口返回信息
     */
    private String msg = "msg";
    /**
     * 请求接口返回数据
     */
    private String data = "data";
    /**
     * 请求接口成功的响应码
     */
    private int successCode = 1;
    /**
     * 配置请求接口的全局参数
     */
    private Map<String, String> globalParameters = new ArrayMap<>();

    /**
     * 初始化各种参数
     *
     * @param code             请求接口返回码
     * @param data             请求接口返回数据
     * @param msg              请求接口返回信息
     * @param successCode      请求接口成功的响应码
     * @param globalParameters 配置请求接口的全局参数
     */
    public void init(String code, String data, String msg, int successCode, Map<String, String> globalParameters) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.successCode = successCode;
        this.globalParameters = globalParameters;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public String getData() {
        return data;
    }


    public int getSuccessCode() {
        return successCode;
    }

    public Map<String, String> getGlobalParameters() {
        return globalParameters;
    }

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
        if (getGlobalParameters() != null) {
            Set<String> set = getGlobalParameters().keySet();
            for (String key : set) {
                requestParams.addBodyParameter(key, getGlobalParameters().get(key));
            }
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

}
