package cn.bluemobi.dylan.fastdev.http;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.orhanobut.logger.Logger;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Set;

import cn.bluemobi.dylan.fastdev.utils.Tools;
import cn.bluemobi.dylan.fastdev.view.LoadingDialog;

/**
 * 网络请求回调
 * Created by yuandl on 2016/8/31 0031.
 */
public class HttpCallBack implements Callback.ProgressCallback<String> {
    private Context context;
    private HttpResponse httpResponse;
    private int requestCode = -1;
    private LoadingDialog loadingDialog;

    public HttpCallBack(Context context, int requestCode, HttpResponse httpResponse) {
        this.context = context;
        this.requestCode = requestCode;
        this.httpResponse = httpResponse;
    }

    public HttpCallBack(Context context, int requestCode, HttpResponse httpResponse, LoadingDialog loadingDialog) {
        this.context = context;
        this.requestCode = requestCode;
        this.httpResponse = httpResponse;
        this.loadingDialog = loadingDialog;
    }

    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {
        if (loadingDialog != null) {
            loadingDialog.show("Loading...");
        }

        httpResponse.netOnStart();
        if (requestCode != -1) {
            httpResponse.netOnStart(requestCode);
        }

    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {

    }

    @Override
    public void onSuccess(String result) {
        Logger.json(result);
        ArrayMap<String, Object> jsonBean;
        try {
            jsonBean = jsonParse(result);
            String msg = Tools.getValue(jsonBean, HttpUtils.getInstance().getMsg());
            int code = Integer.parseInt(Tools.getValue(jsonBean, HttpUtils.getInstance().getCode()));

            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
            if (code == HttpUtils.getInstance().getSuccessCode()) {
                Map<String, Object> data = (Map<String, Object>) jsonBean.get(HttpUtils.getInstance().getData());
                httpResponse.netOnSuccess(data);
                if (requestCode != -1) {
                    httpResponse.netOnSuccess(data, requestCode);
                }
            } else {
                httpResponse.netOnOtherStatus(code, msg);
                if (requestCode != -1) {
                    httpResponse.netOnOtherStatus(code, msg, requestCode);
                }
            }
        } catch (Exception e) {
            Logger.d(result);
            Tools.show(context, "服务器异常！", Toast.LENGTH_SHORT);
            e.printStackTrace();
        } finally {
        }

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        httpResponse.netOnFailure(ex);
        if (requestCode != -1) {
            httpResponse.netOnFailure(requestCode, ex);
        }
        Logger.d(ex.getMessage());
        if (ex instanceof HttpException) { // 网络错误
            HttpException httpEx = (HttpException) ex;
            int responseCode = httpEx.getCode();
            String responseMsg = httpEx.getMessage();
            String errorResult = httpEx.getResult();
            Toast.makeText(x.app(), "网络繁忙", Toast.LENGTH_SHORT).show();
            // ...
        } else if (ex instanceof SocketTimeoutException) {
            Toast.makeText(x.app(), "连接服务器超时", Toast.LENGTH_SHORT).show();
        } else if (ex != null && "网络不可用".equals(ex.getMessage())) {
        } else { // 其他错误
            Toast.makeText(x.app(), "连接服务器失败，请稍后再试！", Toast.LENGTH_SHORT).show();
            // ...
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onFinished() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        httpResponse.netOnFinish();
        if (requestCode != -1) {
            httpResponse.netOnFinish(requestCode);
        }
    }

    private ArrayMap<String, Object> jsonParse(String json) throws JSONException {
        ArrayMap<String, Object> arrayMap = JSON.parseObject(json, new TypeReference<ArrayMap<String, Object>>() {
        }.getType());

        ArrayMap<String, Object> returnData = new ArrayMap<String, Object>();
        ArrayMap<String, Object> rrData = null;
        String dataStrKey = HttpUtils.getInstance().getData();
        if (arrayMap.containsKey(dataStrKey)) {
            Object data = arrayMap.get(dataStrKey);
            if (data instanceof String) {
                returnData.put(dataStrKey, data.toString());
            } else if (data instanceof JSONArray) {
                rrData = new ArrayMap<String, Object>();
                rrData.put(dataStrKey, data);
                returnData.put(dataStrKey, rrData);
            } else if (data instanceof com.alibaba.fastjson.JSONObject) {
                rrData = JSON.parseObject(data.toString(), new TypeReference<ArrayMap<String, Object>>() {
                }.getType());
                returnData.put(dataStrKey, rrData);
            } else {
                returnData.put(dataStrKey, new ArrayMap<>());
            }
        } else {
            rrData = new ArrayMap<>();
            Set<String> keys = arrayMap.keySet();
            for (String s : keys) {
                if (!s.equals(dataStrKey)) {
                    rrData.put(s, arrayMap.get(s));
                }
            }
            returnData.put(dataStrKey, rrData);
        }
        returnData.put(HttpUtils.getInstance().getCode(), Tools.getValue(arrayMap, HttpUtils.getInstance().getCode()));
        returnData.put(HttpUtils.getInstance().getMsg(), Tools.getValue(arrayMap, HttpUtils.getInstance().getMsg()));
        return returnData;
    }
}
