package cn.bluemobi.dylan.smartwebview;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * 此WebViewWithProgress继承自Relativielayout,
 * 如果要设置webview的属性，要先调用getWebView()来取得
 * webview的实例
 */
public class SmartWebView extends RelativeLayout {


    private Context context;
    private WebView mWebView = null;
    //水平进度条
    private ProgressBar progressBar = null;
    //包含圆形进度条的布局
    private RelativeLayout progressBar_circle = null;
    //进度条样式,Circle表示为圆形，Horizontal表示为水平
    private int mProgressStyle = ProgressStyle.Horizontal.ordinal();
    //默认水平进度条高度
    public static int DEFAULT_BAR_HEIGHT = 8;
    //水平进度条的高
    private int mBarHeight = DEFAULT_BAR_HEIGHT;

    /**
     * 成功回调接口
     *
     * @param context
     */
    private WebLoadCallBack loadCallBack;

    /**
     * @param loadCallBack the loadCallBack to set
     */
    public void setLoadCallBack(WebLoadCallBack loadCallBack) {
        this.loadCallBack = loadCallBack;
    }

    public enum ProgressStyle {
        Horizontal,
        Circle;
    }

    ;

    public SmartWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        init();
    }

    public SmartWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.WebViewWithProgress);
        mProgressStyle = attributes.getInt(R.styleable.WebViewWithProgress_progressStyle, ProgressStyle.Horizontal.ordinal());
        mBarHeight = attributes.getDimensionPixelSize(R.styleable.WebViewWithProgress_barHeight, DEFAULT_BAR_HEIGHT);
        attributes.recycle();
        init();
    }

    private void init() {
        mWebView = new WebView(context);
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWebView.getSettings().setLoadsImagesAutomatically(false);
        }
        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setUseWideViewPort(true);
//        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setVerticalScrollbarOverlay(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setHorizontalScrollbarOverlay(false);
        WebSettings webSettings = mWebView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setDefaultTextEncodingName("utf-8");
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setDatabaseEnabled(true);
        String dir = getContext().
                getDir("database", Context.MODE_PRIVATE).getPath();
        mWebView.getSettings().setGeolocationEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setGeolocationDatabasePath(dir);
        setScale(mWebView);
//        setDefaultZoom();
//        //让缩放显示的最小值为起始
        mWebView.setInitialScale(3);
        // 设置支持缩放
        // 设置缩放工具的显示
        webSettings.setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
        webSettings.setSupportZoom(false);

        this.addView(mWebView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        if (mProgressStyle == ProgressStyle.Horizontal.ordinal()) {
            progressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.ac_progress_horizontal, null);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            SmartWebView.this.addView(progressBar, LayoutParams.FILL_PARENT, mBarHeight);
        } else {
            progressBar_circle = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.ac_progress_circle, null);
            SmartWebView.this.addView(progressBar_circle, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

                // 不要使用super，否则有些手机访问不了，因为包含了一条 handler.cancel()
                // super.onReceivedSslError(view, handler, error);

                // 接受所有网站的证书，忽略SSL错误，执行访问网页
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                // TODO Auto-generated method stub
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    if (progressBar_circle != null) {
                        progressBar_circle.setVisibility(View.GONE);
                    }
                    if (loadCallBack != null)
                        loadCallBack.onLoaded(view.getUrl());
                } else {
                    if (mProgressStyle == ProgressStyle.Horizontal.ordinal()) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                    } else {
                        progressBar_circle.setVisibility(View.VISIBLE);
                    }
                }
            }


        });
    }

    /**
     * Description: 自己填写
     *
     * @param url
     */
    public void loadUrl(String url) {
        loadUrl(url, null, null);
        // TODO Auto-generated method stub

    }

    /**
     * Description: 自己填写
     *
     * @param data
     */
    public void loadData(String data) {
        loadUrl(null, data, null);
        // TODO Auto-generated method stub

    }

    /**
     * @param data
     */
    public void loadData(String data, String baseUrl) {
        loadUrl(null, data, baseUrl);
        // TODO Auto-generated method stub

    }

    public void setOfflineCache() {
        WebSettings settings = mWebView.getSettings();
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        //开启DOM缓存，关闭的话H5自身的一些操作是无效的
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
    }

    /**
     * Description: 自己填写
     *
     * @param webChromeClient
     */
    public void setWebChromeClient(WebChromeClient webChromeClient) {
        if (mWebView != null) {
            mWebView.setWebChromeClient(webChromeClient);
        }
        // TODO Auto-generated method stub

    }

    public WebView getWebView() {
        return mWebView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * 加载网页
     *
     * @param url
     */
    protected void loadUrl(String url, String data, String baseUrl) {

//        wv.setWebViewClient(new SslWebViewClient());
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        } else if (!TextUtils.isEmpty(baseUrl)) {
            mWebView.loadDataWithBaseURL(baseUrl, data, "text/html; charset=UTF-8", null, baseUrl);
        } else if (!TextUtils.isEmpty(data)) {
            mWebView.loadData(data, "text/html; charset=UTF-8", null);
        }
    }

    private void setScale(WebView wv) {
        /**
         * 这里需要根据不同的分辨率设置不同的比例,比如
         * 5寸手机设置190  屏幕宽度 > 650   180
         * 4.5寸手机设置170  屏幕宽度>  500 小于 650  160
         * 4寸手机设置150  屏幕宽度>  450 小于 550  150
         * 3           屏幕宽度>  300 小于 450  120
         * 小于    300  100
         *  320×480  480×800 540×960 720×1280
         */
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        if (width > 650) {
            wv.setInitialScale(190);
        } else if (width > 520) {
            wv.setInitialScale(160);
        } else if (width > 450) {
            wv.setInitialScale(140);
        } else if (width > 300) {
            wv.setInitialScale(120);
        } else {
            wv.setInitialScale(100);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // 先从父控件中移除WebView
        removeView(mWebView);
        mWebView.stopLoading();
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.clearHistory();
        mWebView.removeAllViews();
        mWebView.destroy();
        Log.d("WebView", "onDetachedFromWindow");

        super.onDetachedFromWindow();
    }
}
