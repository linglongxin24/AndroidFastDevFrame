package com.dylanfastdev;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;

/**
 * @author YDL
 * @version 1.0
 * @date 2020/04/14/10:00
 */
public class H5Activity extends AppCompatActivity {
    private WebView webView;
    private FrameLayout flVideoContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.ac_h5);
        flVideoContainer = findViewById(R.id.flVideoContainer);
        webView = findViewById(R.id.webView);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
        }
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = webView.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webView.getSettings(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.d("request="+url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.evaluateJavascript("(function(){var meta = document.createElement('meta');meta.content = 'width=device-width, initial-scale=1.0, maximum-scale=1.0,minimum-scale=1.0, user-scalable=no';meta.name = 'viewport';document.getElementsByTagName('head')[0].appendChild(meta);var body = document.getElementsByTagName('body')[0];body.style.cssText = 'width:100%;overflow-x:hidden;font-size:14px;margin:0;padding:10px;box-sizing:border-box;';var tb = document.getElementsByTagName('table');for (var k = 0; k < tb.length; k++){var nd = tb[k];nd.style.cssText += ';color:#101010;font-size: 14px;width:100%;overflow-x:hidden;';};var ps = document.getElementsByTagName('p');for (var j = 0; j < ps.length; j++){var nd = ps[j];var str = nd.innerHTML;str = str.replace(/&nbsp;/g, '');nd.innerHTML = str;nd.style.cssText += ';color:#101010;font-size: 14px;width:100%;overflow-x:hidden;';}})()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
        });
        webView.addJavascriptInterface(new Object(){

            @JavascriptInterface
            public void playing(){
                Log.i("video", "=======================");
                fullScreen();
            }

        }, "local_obj");
        webView.setWebChromeClient(new MyWebChromeClient());// 重写一下，有的时候可能会出现问题
        webView.setBackgroundColor(ContextCompat.getColor(this,android.R.color.transparent));
        webView.setBackgroundResource(R.color.black);
//        webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
//        webView.loadUrl("https://player.bilibili.com/player.html?aid=582731140&bvid=BV1S64y1u7bL&cid=174633886&page=1");
        String url="https://player.bilibili.com/player.html?aid=582731140&bvid=BV1S64y1u7bL&cid=174633886&page=1";
        if(url.startsWith("https://player.bilibili.com/player.html")){
            webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        }
        webView.loadUrl(url);
    }


    private class MyWebChromeClient extends WebChromeClient{
        WebChromeClient.CustomViewCallback mCallback;
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            Log.i("ToVmp","onShowCustomView");
            fullScreen();

            webView.setVisibility(View.GONE);
            flVideoContainer.setVisibility(View.VISIBLE);
            flVideoContainer.addView(view);
            mCallback = callback;
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            Log.i("ToVmp","onHideCustomView");
            fullScreen();

            webView.setVisibility(View.VISIBLE);
            flVideoContainer.setVisibility(View.GONE);
            flVideoContainer.removeAllViews();
            super.onHideCustomView();

        }
    }
    public void fullScreen(){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
