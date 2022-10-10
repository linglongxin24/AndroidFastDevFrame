package cn.bluemobi.dylan.smartwebview

import android.annotation.TargetApi
import android.content.Context
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout

/**
 * 此WebViewWithProgress继承自Relativielayout,
 * 如果要设置webview的属性，要先调用getWebView()来取得
 * webview的实例
 */
open class SmartWebView : RelativeLayout {
    private lateinit var mWebView: WebView

    /**
     * 默认水平进度条高度
     */
    private var DEFAULT_BAR_HEIGHT = 8

    /**
     * 水平进度条高度
     */
    private var mBarHeight = DEFAULT_BAR_HEIGHT

    private var baseWebViewClient: BaseWebViewClient? = null

    companion object {
        /**
         * 水平进度条
         */
        private var progressBar: ProgressBar? = null

        /**
         * 包含圆形进度条的布局
         */
        private var progressBarCircle: RelativeLayout? = null

        /**
         * 进度条样式,Circle表示为圆形，Horizontal表示为水平
         */
        private var mProgressStyle = ProgressStyle.Horizontal.ordinal
    }

    constructor(context: Context) : super(context) {
        initWebView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.WebViewWithProgress)
        mProgressStyle = attributes.getInt(
            R.styleable.WebViewWithProgress_progressStyle,
            ProgressStyle.Horizontal.ordinal
        )
        mBarHeight = attributes.getDimensionPixelSize(
            R.styleable.WebViewWithProgress_barHeight,
            DEFAULT_BAR_HEIGHT
        )
        attributes.recycle()
        initWebView()
    }

    enum class ProgressStyle {
        Horizontal,
        Circle
    }

    private fun initWebView() {
        mWebView = WebView(context)
        apply {
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            setScale(mWebView)
            //让缩放显示的最小值为起始
            mWebView.setInitialScale(3)
            addView(mWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            if (mProgressStyle == ProgressStyle.Horizontal.ordinal) {
                progressBar = LayoutInflater.from(context)
                    .inflate(R.layout.ac_progress_horizontal, null) as ProgressBar
                progressBar?.max = 100
                progressBar?.progress = 0
                addView(progressBar, LayoutParams.MATCH_PARENT, mBarHeight)
            } else {
                progressBarCircle = LayoutInflater.from(context)
                    .inflate(R.layout.ac_progress_circle, null) as RelativeLayout
                addView(progressBarCircle, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            }
            baseWebViewClient = BaseWebViewClient()
            mWebView.webViewClient = baseWebViewClient
            mWebView.webChromeClient = BaseWebChromeClient()
            val webSettings = mWebView.settings
            webSettings.apply {
                loadsImagesAutomatically = Build.VERSION.SDK_INT >= 19
                // android 5.0以上默认不支持Mixed Content
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                }
                layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
                useWideViewPort = true
                //设置WebView属性，能够执行Javascript脚本
                javaScriptEnabled = true
                pluginState = WebSettings.PluginState.ON
                domStorageEnabled = true
                loadWithOverviewMode = true
                savePassword = false
                blockNetworkImage = false//解决图片不显示
                defaultTextEncodingName = "UTF-8"
                //设置可以访问文件
                allowFileAccess = false
                cacheMode = WebSettings.LOAD_NO_CACHE
                layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                databaseEnabled = true
                val dir = context.getDir("database", Context.MODE_PRIVATE).getPath()
                setGeolocationEnabled(true)
                javaScriptCanOpenWindowsAutomatically = true
                setGeolocationDatabasePath(dir)
                // 设置支持缩放
                // 设置缩放工具的显示
                builtInZoomControls = false
                webSettings.allowUniversalAccessFromFileURLs = false
                webSettings.allowFileAccessFromFileURLs = false
                setSupportZoom(false)
                val errorPageView =
                    LayoutInflater.from(context).inflate(R.layout.default_error_page, null)
                errorPageView.findViewById<Button>(R.id.bt_error_retry).setOnClickListener {
                    refresh()
                }
                setErrorPage(errorPageView)
            }
        }
    }

    /**
     * Description: 自己填写
     *
     * @param url
     */
    fun loadUrl(url: String) {
        loadUrl(url, null, null)
    }

    /**
     * Description: 自己填写
     *
     * @param data
     */
    fun loadData(data: String) {
        loadUrl(null, data, null)
    }

    /**
     * @param data
     */
    fun loadData(data: String, baseUrl: String) {
        loadUrl(null, data, baseUrl)
    }

    /**
     * 加载网页
     *
     * @param url
     */
    private fun loadUrl(url: String?, data: String?, baseUrl: String?) {
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url)
        } else if (!TextUtils.isEmpty(baseUrl)) {
            mWebView.loadDataWithBaseURL(baseUrl, data, "text/html", "UTF-8", baseUrl)
        } else if (!TextUtils.isEmpty(data)) {
            mWebView.loadData(data, "text/html", "UTF-8")
        }
    }

    fun setOfflineCache() {
        var settings = mWebView.settings
        settings.setAppCacheEnabled(true)
        settings.databaseEnabled = true
        //开启DOM缓存，关闭的话H5自身的一些操作是无效的
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
    }

    /**
     * 设置webChromeClient
     *
     * @param webViewClient
     */
    fun setWebViewClient(webViewClient: BaseWebViewClient) {
        baseWebViewClient = webViewClient
        mWebView.webViewClient = webViewClient
        errorView?.let { setErrorPage(it) }
    }

    open class BaseWebViewClient() : WebViewClient() {
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            // 不要使用super，否则有些手机访问不了，因为包含了一条 handler.cancel()
            // super.onReceivedSslError(view, handler, error)
            // 接受所有网站的证书，忽略SSL错误，执行访问网页
            handler?.proceed()
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onReceivedError(
            webView: WebView?,
            webResourceRequest: WebResourceRequest,
            webResourceError: WebResourceError?
        ) {
            super.onReceivedError(webView, webResourceRequest, webResourceError)
            if (webResourceRequest.isForMainFrame) {
                showErrorPage(webView)
            }
        }

        private var isError = false
        private var errorView: View? = null
        fun setErrorPage(errorView: View) {
            this.errorView = errorView
        }

        private fun showErrorPage(webView: WebView?) {
            isError = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView?.evaluateJavascript(
                    "javascript:document.open();document.write('');document.close();",
                    null
                )
            }
            errorView?.let {
                errorView?.visibility = View.VISIBLE
                webView?.visibility = View.GONE
            }
        }

        private fun showNormalPage(view: WebView?) {
            errorView?.let {
                errorView?.visibility = View.GONE
                view?.visibility = View.VISIBLE
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar?.postDelayed({ progressBar?.visibility = View.GONE }, 10)
            if (isError) {
                showErrorPage(view)
            } else {
                showNormalPage(view)
            }
            isError = false
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            // 4.0以上必须要加
            if (url.startsWith("http://") || url.startsWith("https://")) {
                return true
            } else if ("bilibili".equals(Uri.parse(url).scheme, true)) {
                return true
            }
            return false
        }
    }

    fun refresh() {
        mWebView.reload()
    }

    private var errorView: View? = null
    fun setErrorPage(errorView: View) {
        this.errorView = errorView
        errorView.visibility = GONE
        if (errorView.parent != null) {
            (errorView.parent as ViewGroup).removeView(errorView)
        }
        addView(errorView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        baseWebViewClient?.setErrorPage(errorView)
    }

    /**
     * 设置webChromeClient
     *
     * @param webChromeClient
     */
    fun setWebChromeClient(webChromeClient: BaseWebChromeClient) {
        mWebView.webChromeClient = webChromeClient
    }

    open class BaseWebChromeClient : WebChromeClient() {
        override fun onGeolocationPermissionsShowPrompt(
            origin: String,
            callback: GeolocationPermissions.Callback
        ) {
            super.onGeolocationPermissionsShowPrompt(origin, callback)
            callback.invoke(origin, true, false)
        }
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress == 100) {
                progressBar?.postDelayed({ progressBar?.visibility = View.GONE }, 10)
                progressBarCircle?.postDelayed({ progressBarCircle?.visibility = View.GONE }, 10)
            } else {
                if (mProgressStyle == ProgressStyle.Horizontal.ordinal) {
                    progressBar?.apply {
                        progress = newProgress
                        takeIf { visibility== GONE }?.apply {
                            progressBar?.visibility = View.VISIBLE
                        }
                    }
                } else {
                    progressBarCircle?.postDelayed({ progressBarCircle?.visibility = View.VISIBLE }, 10)
                }
            }
        }
    }

    fun getWebView(): WebView {
        return mWebView
    }

    fun getProgressBar(): ProgressBar? {
        return progressBar
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
        var wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var width = wm.defaultDisplay.width
        if (width > 650) {
            webView.setInitialScale(190)
        } else if (width > 520) {
            webView.setInitialScale(160)
        } else if (width > 450) {
            webView.setInitialScale(140)
        } else if (width > 300) {
            webView.setInitialScale(120)
        } else {
            webView.setInitialScale(100)
        }
    }

    override fun onDetachedFromWindow() {
        // 先从父控件中移除WebView
        removeView(mWebView)
        mWebView.stopLoading()
        mWebView.settings.javaScriptEnabled = false
        mWebView.clearHistory()
        mWebView.removeAllViews()
        mWebView.destroy()
        super.onDetachedFromWindow()
    }
}
