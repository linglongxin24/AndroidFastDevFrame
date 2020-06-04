package com.dylanfastdev;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.orhanobut.logger.Logger;

import java.util.Arrays;

import cn.bluemobi.dylan.base.utils.Tools;
import cn.bluemobi.dylan.base.*;

public class WebViewActivity extends BaseActivity {
    private InsideWebChromeClient mInsideWebChromeClient;
    private String url, action, titleName, createTime, type;
    private boolean mNew;
    private int playCount;
    private String jsData = "";

    private WebView webview;
    private FrameLayout  mFrameLayout;

    @Override
    public void initTitleBar() {
        setTitle("存储");
    }

    @Override
    protected int getContentView() {
        return R.layout.ac_webview2;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        webview = findViewById(R.id.webView);
        mFrameLayout = findViewById(R.id.flVideoContainer);
    }

    @SuppressLint({"SetJavaScriptEnabled", "SetTextI18n"})
    @Override
    public void initData() {
//        if (null == getIntent().getExtras()) return;
//        url = getIntent().getExtras().getString("url");
        url = "https://player.bilibili.com/player.html?aid=582731140&bvid=BV1S64y1u7bL&cid=174633886&page=1";
        mInsideWebChromeClient = new InsideWebChromeClient();
        InsideWebViewClient mInsideWebViewClient = new InsideWebViewClient();
        webview.setWebViewClient(mInsideWebViewClient);//IE内核
        webview.setWebChromeClient(mInsideWebChromeClient);
//        webview.loadUrl(jsData);
        webview.loadUrl(url);

        WebSettings setting = webview.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setAppCacheEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setSupportZoom(false);//不支持缩放
        setting.setBuiltInZoomControls(false);//不出现放大和缩小的按钮
        setting.setPluginState(WebSettings.PluginState.ON);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        //settings.setPluginsEnabled(true);
        setting.setAllowFileAccess(false);
//        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        Logger.d("Tools.getScreenSize()="+ Arrays.toString(Tools.getScreenSize(mContext)));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }

    }

    @Override
    public void addListener() {

    }

    @Override
    public void onClick(View v) {

    }

    private class InsideWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            mFrameLayout.addView(mCustomView);
            mCustomViewCallback = callback;
            webview.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        public void onHideCustomView() {
            webview.setVisibility(View.VISIBLE);
            if (mCustomView == null) {
                return;
            }
            mCustomView.setVisibility(View.GONE);
            mFrameLayout.removeView(mCustomView);
            mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onHideCustomView();
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Logger.d("url:" + url + ";message:" + message);
            return super.onJsAlert(view, url, message, result);
        }
    }

    private class InsideWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
//            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //mWebView.loadUrl(javascript);
            view.evaluateJavascript("(function(){var meta = document.createElement('meta');meta.content = 'width=device-width, initial-scale=1.0, maximum-scale=1.0,minimum-scale=1.0, user-scalable=no';meta.name = 'viewport';document.getElementsByTagName('head')[0].appendChild(meta);var body = document.getElementsByTagName('body')[0];body.style.cssText = 'width:100%;overflow-x:hidden;font-size:14px;margin:0;padding:10px;box-sizing:border-box;';var tb = document.getElementsByTagName('table');for (var k = 0; k < tb.length; k++){var nd = tb[k];nd.style.cssText += ';color:#101010;font-size: 14px;width:100%;overflow-x:hidden;';};var ps = document.getElementsByTagName('p');for (var j = 0; j < ps.length; j++){var nd = ps[j];var str = nd.innerHTML;str = str.replace(/&nbsp;/g, '');nd.innerHTML = str;nd.style.cssText += ';color:#101010;font-size: 14px;width:100%;overflow-x:hidden;';}})()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        webview.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webview.onResume();
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        webview.destroy();
        super.onDestroy();
    }
}
