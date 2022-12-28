package cn.bluemobi.dylan.http;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.bjtsn.dylan.lifecycleobserver.LifecycleCallback;
import com.bjtsn.dylan.lifecycleobserver.LifecycleObserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.bluemobi.dylan.http.dialog.DialogOnDismissListener;
import cn.bluemobi.dylan.http.dialog.DialogOnKeyListener;
import cn.bluemobi.dylan.http.dialog.LoadingDialog;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Response;
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

    public HttpRequest setShowLoadingDialog(boolean showLoadingDialog) {
        isShowLoadingDialog = showLoadingDialog;
        return this;
    }

    /**
     * 默认显示加载进度对话框
     */
    private boolean isShowLoadingDialog = true;

    /**
     * 接口监听者
     */
    private Observable<? extends Response<ResponseBody>> observable;
    private LoadingDialog loadingDialog = null;

    public LoadingDialog getLoadingDialog() {
        return loadingDialog;
    }

    /**
     * 【第一步】设置上下文
     *
     * @param context 上下文
     * @return 本类对象
     */
    public HttpRequest(Context context) {
        this.context = new WeakReference<Context>(context);
        httpJsonKey = JsonParse.getJsonParse();
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

    public HttpRequest setTimeout(long timeout, TimeUnit unit) {
        RetrofitManager.getRetrofitManager().setTimeout(timeout, unit);
        return this;
    }

    /**
     * 【第三步】设置访问的几口接口
     *
     * @param observable 接口对象
     * @return 本类对象
     */
    public HttpRequest setObservable(Observable<? extends Response<ResponseBody>> observable) {
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
        isShowOtherStatusMessage = false;
        return this;
    }

    private boolean isShowFailMessage = true;

    public HttpRequest hideFailMessage() {
        isShowFailMessage = false;
        return this;
    }

    public boolean isShowSuccessMessage() {
        return isShowSuccessMessage;
    }

    public HttpRequest setShowSuccessMessage(boolean showSuccessMessage) {
        isShowSuccessMessage = showSuccessMessage;
        return this;
    }

    public boolean isShowOtherStatusMessage() {
        return isShowOtherStatusMessage;
    }

    public HttpRequest setShowOtherStatusMessage(boolean showOtherStatusMessage) {
        isShowOtherStatusMessage = showOtherStatusMessage;
        return this;
    }

    public boolean isShowFailMessage() {
        return isShowFailMessage;
    }

    public HttpRequest setShowFailMessage(boolean showFailMessage) {
        isShowFailMessage = showFailMessage;
        return this;
    }

    private boolean canCancel = true;

    public HttpRequest setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
        return this;
    }

    private String loadingMessage;

    public HttpRequest setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
        return this;
    }

    private ResponseInterceptor responseInterceptor;

    public HttpRequest setResponseInterceptor(ResponseInterceptor responseInterceptor) {
        this.responseInterceptor = responseInterceptor;
        return this;
    }

    private  CustomResponse customResponse;
    public void setCustomResponse(CustomResponse customResponse){
        this.customResponse=customResponse;
    }
    private HttpJsonKey httpJsonKey;

    public HttpRequest setHttpJsonKey(HttpJsonKey httpJsonKey) {
        this.httpJsonKey = httpJsonKey;
        return this;
    }

    /**
     * 【第四步】设置访问接口的返回监听
     *
     * @param httpResponse 请求相应监听
     * @return 本类对象
     */
    public Subscription setDataListener(final HttpCallBack httpResponse) {
        final Context context = this.context.get();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isDestroyed()) {
                    httpResponse.netOnFinish();
                    return null;
                }
            }
        }
        String network_unusual = MessageManager.getMessageManager().isUseEnglishLanguage() ? "Network  unusual" : "网络不可用";
        if (customResponse==null&&!NetworkUtil.isNetworkAvailable(context)) {
            Toast.makeText(context, network_unusual, Toast.LENGTH_SHORT).show();
            if (httpResponse != null) {
                httpResponse.netOnFailure(new Exception(network_unusual));
                httpResponse.netOnFinish();
            }
            return null;
        }

        if (isShowLoadingDialog) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(context);
            }
        } else {
            loadingDialog = null;
        }
        final Subscription subscribe = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<ResponseBody>>() {

                    @Override
                    public void onStart() {
                        if (loadingDialog != null) {
                            loadingDialog.show(loadingMessage);
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
                        if (Http.getHttp().isDebugMode()) {
                            e.printStackTrace();
                        }
                        onCompleted();
                        if(customResponse!=null){
                            customResponse.onError(context,HttpRequest.this,httpResponse,e);
                            return;
                        }
                        if (responseInterceptor != null) {
                            responseInterceptor.onError(context, e);
                        }
                        if (isShowFailMessage) {
                            if (e instanceof SocketTimeoutException) {
                                Toast.makeText(context, "网络连接超时,请重新再试", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "网络连接错误,错误码：-2", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (httpResponse != null) {
                            httpResponse.netOnFailure(e);
                        }

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        if(customResponse!=null){
                            customResponse.onNext(context,HttpRequest.this,httpResponse,responseBodyResponse);
                            return;
                        }
                        String responseString = "";
                        boolean isSuccessful = responseBodyResponse.isSuccessful();
                        ArrayMap<String, Object> jsonBean;
                        if (!isSuccessful) {
                            try {
                                responseString = responseBodyResponse.errorBody().string();
                            } catch (IOException e) {
                                showErrorMessage("读取数据错误，错误码：0", e);
                            }
                        } else {
                            try {
                                responseString = responseBodyResponse.body().string();
                            } catch (IOException e) {
                                showErrorMessage("读取数据错误，错误码：0", e);
                            }
                        }
                        if (responseInterceptor != null) {
                            Map<String, Object> requestParameter = getRequestParement(responseBodyResponse.raw().request());
                            boolean isInterceptor = responseInterceptor.onResponseStart(context, responseBodyResponse.raw().request().url().toString(), requestParameter, responseString, responseBodyResponse.raw().code());
                            if (isInterceptor) {
                                return;
                            }
                        }
                        if (isSuccessful) {
                            //200
                            try {
                                jsonBean = JsonParse.getJsonParse().jsonParse(responseString, httpJsonKey.getCode(), httpJsonKey.getData(), httpJsonKey.getMsg());
                                String msg = JsonParse.getString(jsonBean, httpJsonKey.getMsg());
                                int code = JsonParse.getInt(jsonBean, httpJsonKey.getCode());
                                Map<String, Object> data = JsonParse.getMap(jsonBean, httpJsonKey.getData());
                                if (responseInterceptor != null) {
                                    boolean isInterceptor = responseInterceptor.onResponse(context, code, msg, data, responseBodyResponse.raw().request().url().url().toString());
                                    if (isInterceptor) {
                                        return;
                                    }
                                }
                                boolean msgIsNotNull = (!TextUtils.isEmpty(msg) && !"null".equalsIgnoreCase(msg));
                                if (code == httpJsonKey.getSuccessCode()) {
                                    if (MessageManager.getMessageManager().getShowMessageModel() == MessageManager.MessageModel.All && isShowSuccessMessage) {
                                        if (msgIsNotNull) {
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    if (httpResponse != null) {
                                        httpResponse.netOnSuccess(data);
                                        httpResponse.netOnSuccess(data, msg);
                                    }
                                } else {
                                    if (MessageManager.getMessageManager().getShowMessageModel() != MessageManager.MessageModel.NO && isShowOtherStatusMessage) {
                                        if (msgIsNotNull) {
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    if (httpResponse != null) {
                                        httpResponse.netOnOtherStatus(code, msg);
                                        httpResponse.netOnOtherStatus(code, msg, data);
                                    }
                                }
                            } catch (JSONException e) {
                                showErrorMessage("服务器数据错误，错误码：1", new JSONException(responseString));
                            } catch (Exception e) {
                                showErrorMessage("其他错误，错误码：-1", e);
                            }
                        } else {
                            if (MessageManager.getMessageManager().getShowMessageModel() != MessageManager.MessageModel.NO && isShowFailMessage) {
                                Toast.makeText(context, "连接服务器错误,错误码：" + responseBodyResponse.code(), Toast.LENGTH_SHORT).show();
                            }
                            if (httpResponse != null) {
                                httpResponse.netOnSuccessServerError(responseBodyResponse.code(), responseBodyResponse.message());
                            }
                        }
                    }

                    /**
                     * 显示错误信息
                     * @param errorMessage 错误信息
                     * @param e 异常
                     */
                    private void showErrorMessage(String errorMessage, Exception e) {
                        if (responseInterceptor != null) {
                            responseInterceptor.onError(context, e);
                        }
                        if (MessageManager.getMessageManager().getShowMessageModel() != MessageManager.MessageModel.NO && isShowFailMessage) {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                        if (httpResponse != null) {
                            httpResponse.netOnFailure(e);
                        }
                        if (Http.getHttp().isDebugMode()) {
                            e.printStackTrace();
                        }
                    }
                });

        if (subscribe != null) {
            if (loadingDialog != null) {
                loadingDialog.setOnKeyListener(new DialogOnKeyListener(loadingDialog, canCancel));
                loadingDialog.setOnDismissListener(new DialogOnDismissListener(subscribe));
            } else {
                addLifeCycle(context, subscribe);
            }
        }
        return subscribe;
    }

    /**
     * 【第四步】设置访问接口的返回监听
     *
     * @param httpResponse 请求相应监听
     * @return 本类对象
     */
    public Subscription setDataListener(final OriginalHttpResponse httpResponse) {
        String network_unusual = MessageManager.getMessageManager().isUseEnglishLanguage() ? "Network  unusual" : "网络不可用";
        final String network_error = MessageManager.getMessageManager().isUseEnglishLanguage() ? "Network  error" : MessageManager.getMessageManager().getErrorMessage();

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
        final Subscription subscribe = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<ResponseBody>>() {

                    @Override
                    public void onStart() {
                        if (loadingDialog != null) {
                            loadingDialog.show(loadingMessage);
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
                        if (Http.getHttp().isDebugMode()) {
                            e.printStackTrace();
                        }
                        if (isShowFailMessage) {
                            Toast.makeText(context.get(), network_error, Toast.LENGTH_SHORT).show();
                        }
                        if (httpResponse != null) {
                            httpResponse.netOnFailure(e);
                        }
                        onCompleted();

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        String responseString = "";
                        boolean isSuccessful = responseBodyResponse.isSuccessful();
                        try {
                            if (!isSuccessful) {
                                responseString = responseBodyResponse.errorBody().string();
                            } else {

                                responseString = responseBodyResponse.body().string();
                            }
                            if (responseInterceptor != null) {
                                Map<String, Object> requestParameter = getRequestParement(responseBodyResponse.raw().request());
                                boolean isInterceptor = responseInterceptor.onResponseStart(context.get(), responseBodyResponse.raw().request().url().url().toString(), requestParameter, responseString, responseBodyResponse.raw().code());
                                if (isInterceptor) {
                                    return;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (httpResponse != null) {
                                httpResponse.netOnSuccess(responseString);
                            }
                        } catch (Exception e) {
                            if (MessageManager.getMessageManager().getShowMessageModel() != MessageManager.MessageModel.NO && isShowFailMessage) {
                                Toast.makeText(context.get(), network_error, Toast.LENGTH_SHORT).show();
                            }
                            if (httpResponse != null) {
                                httpResponse.netOnFailure(e);
                            }
                            if (Http.getHttp().isDebugMode()) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        if (subscribe != null) {
            if (loadingDialog != null) {
                loadingDialog.setOnKeyListener(new DialogOnKeyListener(loadingDialog, canCancel));
                loadingDialog.setOnDismissListener(new DialogOnDismissListener(subscribe));
            } else {
                addLifeCycle(context.get(), subscribe);
            }
        }
        return subscribe;
    }

    private void addLifeCycle(Context mContext, final Subscription subscribe) {
        if (mContext instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) mContext;
            new LifecycleObserver(activity).observer(new LifecycleCallback() {
                @Override
                public void onDestroy() {
                    if (subscribe != null && !subscribe.isUnsubscribed()) {
                        subscribe.unsubscribe();
                    }
                    super.onDestroy();
                }
            });
        }

    }

    private Map<String, Object> getRequestParement(Request original) {
        Map<String, Object> map = new ArrayMap<>();
        //请求体定制：统一添加sign参数
        if (original.body() instanceof FormBody) {
            FormBody oidFormBody = (FormBody) original.body();
            for (int i = 0; i < oidFormBody.size(); i++) {
                String name = oidFormBody.name(i);
                String value = oidFormBody.value(i);
                map.put(name, value);
            }
        } else if (original.body() instanceof MultipartBody) {
            MultipartBody multipartBody = (MultipartBody) original.body();
            for (MultipartBody.Part part : multipartBody.parts()) {
                String name = getPartHeaders(part);
                String value = null;
                try {
                    value = URLDecoder.decode(getRequestBody(part).replaceAll("%(?![0-9a-fA-F]{2})", "%25"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                map.put(name, value);
            }
        }
        return map;
    }

    private String getPartHeaders(MultipartBody.Part part) {
        Class<?> personType = part.getClass();

        //访问私有属性
        Field field = null;
        try {
            field = personType.getDeclaredField("headers");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        field.setAccessible(true);
        try {
            Headers headers = (Headers) field.get(part);
            String s = headers.get("content-disposition");
            return s.split(";")[1].split("=")[1].replace("\"", "");

        } catch (IllegalAccessException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }

    private String getRequestBody(MultipartBody.Part part) {

        Class<?> personType = part.getClass();

        //访问私有属性
        Field field = null;
        try {
            field = personType.getDeclaredField("body");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        field.setAccessible(true);
        try {
            RequestBody requestBody = (RequestBody) field.get(part);
            MediaType contentType = requestBody.contentType();
            if (contentType.type().equals("multipart")) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset UTF8 = Charset.forName("UTF-8");
                Charset charset = contentType.charset(UTF8);
                return buffer.readString(charset);
            } else if (contentType.type().equals("image")) {
                return convertFileSize(requestBody.contentLength());
//                Class<?> requestBodyClass = requestBody.getClass();
//
//                //访问私有属性
//                Field fileRequestBodyClass = null;
//                try {
//                    fileRequestBodyClass = requestBodyClass.getDeclaredField("file");
//
//
//                    fileRequestBodyClass.setAccessible(true);
//                    File file = (File) fileRequestBodyClass.get(requestBody);
//                    Logger.d("file=" + file.getPath());
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

}
