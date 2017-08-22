package cn.bluemobi.dylan.http;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Map;

import cn.bluemobi.dylan.http.dialog.LoadingDialog;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * http请求解析类
 * Created by yuandl on 2017-03-31.
 */

public class HttpRequest {

    /**
     * 要显示的对话框的上下文
     */
    private WeakReference<Context> context;

    /**
     * 默认显示加载进度对话框
     */
    private boolean isShowLoadingDialog = true;

    /**
     * 接口监听者
     */
    private Observable<ResponseBody> observable;
    private LoadingDialog loadingDialog = null;


    /**
     * 【第一步】设置上下文
     *
     * @param context 上下文
     * @return 本类对象
     */
    public HttpRequest(Context context) {
        this.context = new WeakReference<Context>(context);
    }

    /**
     * 【第二步】设置隐藏加载对话框（可忽略此步骤，默认显示）
     *
     * @return 本类对象
     */
    public HttpRequest hideLoadingDialog() {
        isShowLoadingDialog = false;
        return this;
    }

    /**
     * 【第三步】设置访问的几口接口
     *
     * @param observable 接口对象
     * @return 本类对象
     */
    public HttpRequest setObservable(Observable<ResponseBody> observable) {
        this.observable = observable;
        return this;
    }

    private boolean isShowSuccessMessage = true;

    public HttpRequest hideSuccessMessage() {
        isShowSuccessMessage = false;
        return this;
    }


    private boolean isShowOtherStatusMessage = true;

    public HttpRequest hideOtherStatusMessage() {
        isShowSuccessMessage = false;
        return this;
    }

    /**
     * 【第四步】设置访问接口的返回监听
     *
     * @param httpResponse 请求相应监听
     * @return 本类对象
     */
    public Subscription setDataListener(final HttpResponse httpResponse) {
        String network_unusual = MessageManager.getMessageManager().isUseEnglishLanguage() ? "Network  unusual" : "网络不可用";
        final String network_error = MessageManager.getMessageManager().isUseEnglishLanguage() ? "Network  error" : "网络繁忙";

        if (!NetworkUtil.isNetworkAvailable(context.get())) {
            Toast.makeText(context.get(), network_unusual, Toast.LENGTH_SHORT).show();
            if (httpResponse != null) {
                httpResponse.netOnFailure(new Exception(network_unusual));
                httpResponse.netOnFinish();
            }
            return null;
        }

        if (isShowLoadingDialog) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(context.get());
            }
        } else {
            loadingDialog = null;
        }
        Subscription subscribe = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {

                    @Override
                    public void onStart() {
                        if (loadingDialog != null) {
                            loadingDialog.show();
                        }
                        if (httpResponse != null) {
                            httpResponse.netOnStart();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        if (httpResponse != null) {
                            httpResponse.netOnFinish();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(context.get(), network_error, Toast.LENGTH_SHORT).show();
                        if (httpResponse != null) {
                            httpResponse.netOnFailure(e);
                        }
                        onCompleted();

                    }

                    @Override
                    public void onNext(ResponseBody result) {
                        ArrayMap<String, Object> jsonBean;
                        try {
                            jsonBean = JsonParse.getJsonParse().jsonParse(result.string());
                            String msg = JsonParse.getString(jsonBean, JsonParse.getJsonParse().getMsg());
                            int code = Integer.parseInt(JsonParse.getString(jsonBean, JsonParse.getJsonParse().getCode()));

                            if (MessageManager.getMessageManager().getShowMessageModel() == MessageManager.MessageModel.All && isShowSuccessMessage) {
                                if (msg != null && !msg.isEmpty() && !"null".equalsIgnoreCase(msg)) {
                                    Toast.makeText(context.get(), msg, Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (code == JsonParse.getJsonParse().getSuccessCode()) {
                                Map<String, Object> data = (Map<String, Object>) jsonBean.get(JsonParse.getJsonParse().getData());

                                if (httpResponse != null) {
                                    httpResponse.netOnSuccess(data);
                                }
                            } else {
                                if (MessageManager.getMessageManager().getShowMessageModel() == MessageManager.MessageModel.OTHER_STATUS && isShowOtherStatusMessage) {
                                    if (msg != null && !msg.isEmpty() && !"null".equalsIgnoreCase(msg)) {
                                        Toast.makeText(context.get(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (httpResponse != null) {
                                    httpResponse.netOnOtherStatus(code, msg);
                                }
                            }
                        } catch (Exception e) {
                            if (MessageManager.getMessageManager().getShowMessageModel() != MessageManager.MessageModel.NO) {
                                Toast.makeText(context.get(), network_error, Toast.LENGTH_SHORT).show();
                            }
                            if (httpResponse != null) {
                                httpResponse.netOnFailure(e);
                            }
                            e.printStackTrace();
                        }
                    }
                });

        if (loadingDialog != null && subscribe != null) {
            loadingDialog.setOnKeyListener(new DialogOnKeyListener(loadingDialog, subscribe));
        }
        return subscribe;
    }

}
