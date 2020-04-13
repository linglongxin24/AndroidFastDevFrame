package com.dylanfastdev;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.WebView;

import com.orhanobut.logger.Logger;

import cn.bluemobi.dylan.base.BaseActivity;
import cn.bluemobi.dylan.smartwebview.SmartWebView;

/**
 * @author dylan
 * @date 2019-07-16
 */
public class WebViewActivity extends BaseActivity {
    private SmartWebView smartWebView;

    @Override
    public void initTitleBar() {
        setTitle("趣头条");
    }

    @Override
    protected int getContentView() {
        return R.layout.ac_webview;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        smartWebView = findViewById(R.id.smartWebView);
    }

    @Override
    public void initData() {
        smartWebView.loadUrl("https://www.bilibili.com/video/BV1S64y1u7bL");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void addListener() {
        final WebView webView = smartWebView.getWebView();
        webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // webview的高度
                int webcontent = (int) (webView.getContentHeight() * webView.getScale());
                // 当前webview的高度
                int webnow = webView.getHeight() +scrollY;
                Logger.d("webcontent=" + webcontent
                        + "\n" + "webnow=" + webnow
                        + "\n" + "scrollY=" + scrollY
                );
                if (Math.abs(webcontent - webnow) < 1) {
                    //处于底端
                    Logger.d("处于底端");
                } else if (scrollY == 0) {
                    Logger.d("处于顶端");
                } else {
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
