package cn.bluemobi.dylan.smartwebview;

import android.content.Context
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.ProgressBar
import android.widget.RelativeLayout

/**
 * 此WebViewWithProgress继承自Relativielayout,
 * 如果要设置webview的属性，要先调用getWebView()来取得
 * webview的实例
 */
open class SmartWebView : RelativeLayout {
    private lateinit var mWebView: WebView;

    //水平进度条
    private var progressBar: ProgressBar? = null;

    //包含圆形进度条的布局
    private var progressBarCircle: RelativeLayout? = null;

    //进度条样式,Circle表示为圆形，Horizontal表示为水平
    private var mProgressStyle = ProgressStyle.Horizontal.ordinal;

    //默认水平进度条高度
    private var DEFAULT_BAR_HEIGHT = 8;

    //水平进度条的高
    private var mBarHeight = DEFAULT_BAR_HEIGHT;

    /**
     * 成功回调接口
     *
     */
    private  var loadCallBack: WebLoadCallBack?=null;

    constructor(context: Context) : super(context) {
        initWebView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.WebViewWithProgress);
        mProgressStyle = attributes.getInt(R.styleable.WebViewWithProgress_progressStyle, ProgressStyle.Horizontal.ordinal);
        mBarHeight = attributes.getDimensionPixelSize(R.styleable.WebViewWithProgress_barHeight, DEFAULT_BAR_HEIGHT);
        attributes.recycle();
        initWebView()
    }

    /**
     * @param loadCallBack the loadCallBack to set
     */
    fun setLoadCallBack(loadCallBack: WebLoadCallBack) {
        this.loadCallBack = loadCallBack;
    }

    enum class ProgressStyle {
        Horizontal,
        Circle;
    }

    fun initWebView() {
        mWebView = WebView(context);
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
        var webSettings = mWebView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSavePassword(false);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setDefaultTextEncodingName("utf-8");
        //设置可以访问文件
        webSettings.setAllowFileAccess(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setDatabaseEnabled(true);
        var dir = getContext().getDir("database", Context.MODE_PRIVATE).getPath();
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
            webSettings.setAllowUniversalAccessFromFileURLs(false)
            webSettings.setAllowFileAccessFromFileURLs(false);
        };
        webSettings.setSupportZoom(false);

        this.addView(mWebView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        if (mProgressStyle == ProgressStyle.Horizontal.ordinal) {
            progressBar = LayoutInflater.from(context).inflate(R.layout.ac_progress_horizontal, null) as ProgressBar
            progressBar?.max = 100;
            progressBar?.progress = 0;
            addView(progressBar, LayoutParams.FILL_PARENT, mBarHeight);
        } else {
            progressBarCircle = LayoutInflater.from(context).inflate(R.layout.ac_progress_circle, null) as RelativeLayout;
            addView(progressBarCircle, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        }
        mWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {

                // 不要使用super，否则有些手机访问不了，因为包含了一条 handler.cancel()
                // super.onReceivedSslError(view, handler, error);

                // 接受所有网站的证书，忽略SSL错误，执行访问网页
                handler?.proceed();
            }

            override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
                webView.loadUrl(url)
                // 4.0以上必须要加
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    return true;
                } else if ("bilibili".equals(Uri.parse(url).getScheme(), true)) {
                    return true;
                }
                return false;
            }
        };
        mWebView.webChromeClient = object : WebChromeClient() {

            override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar?.visibility = View.GONE;
                    progressBarCircle?.visibility = View.GONE
                    loadCallBack?.onLoaded(view.url)
                } else {
                    if (mProgressStyle == ProgressStyle.Horizontal.ordinal) {
                        progressBar?.visibility = View.VISIBLE;
                        progressBar?.progress = newProgress;
                    } else {
                        progressBarCircle?.visibility = View.VISIBLE;
                    }
                }
            }


        };
    }

    /**
     * Description: 自己填写
     *
     * @param url
     */
    fun loadUrl(url: String) {
        loadUrl(url, null, null);
    }

    /**
     * Description: 自己填写
     *
     * @param data
     */
    fun loadData(data: String) {
        loadUrl(null, data, null);
    }

    /**
     * @param data
     */
    fun loadData(data: String, baseUrl: String) {
        loadUrl(null, data, baseUrl);
    }

    /**
     * 加载网页
     *
     * @param url
     */
    protected fun loadUrl(url: String?, data: String?, baseUrl: String?) {

//        wv.setWebViewClient(new SslWebViewClient());
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        } else if (!TextUtils.isEmpty(baseUrl)) {
            mWebView.loadDataWithBaseURL(baseUrl, data, "text/html; charset=UTF-8", null, baseUrl);
        } else if (!TextUtils.isEmpty(data)) {
            mWebView.loadData(data, "text/html; charset=UTF-8", null);
        }
    }

    fun setOfflineCache() {
        var settings = mWebView.getSettings();
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
    fun setWebChromeClient(webChromeClient: WebChromeClient) {
        if (mWebView != null) {
            mWebView.setWebChromeClient(webChromeClient);
        }
    }

    fun getWebView(): WebView {
        return mWebView;
    }

    fun getProgressBar(): ProgressBar? {
        return progressBar;
    }


    private fun setScale(webView: WebView) {
        /**
         * 这里需要根据不同的分辨率设置不同的比例,比如
         * 5寸手机设置190  屏幕宽度 > 650   180
         * 4.5寸手机设置170  屏幕宽度>  500 小于 650  160
         * 4寸手机设置150  屏幕宽度>  450 小于 550  150
         * 3           屏幕宽度>  300 小于 450  120
         * 小于    300  100
         *  320×480  480×800 540×960 720×1280
         */
        var wm = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager;
        var width = wm.getDefaultDisplay().getWidth();
        if (width > 650) {
            webView.setInitialScale(190);
        } else if (width > 520) {
            webView.setInitialScale(160);
        } else if (width > 450) {
            webView.setInitialScale(140);
        } else if (width > 300) {
            webView.setInitialScale(120);
        } else {
            webView.setInitialScale(100);
        }
    }

    override fun onDetachedFromWindow() {
        // 先从父控件中移除WebView
        removeView(mWebView);
        mWebView.stopLoading();
        mWebView.settings.javaScriptEnabled = false;
        mWebView.clearHistory();
        mWebView.removeAllViews();
        mWebView.destroy();
        super.onDetachedFromWindow();
    }
}
