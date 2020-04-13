package com.dylanfastdev;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.WebSettings;
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
//        smartWebView.loadUrl("http:192.168.1.186:8080/video.html");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void addListener() {
        final WebView webView = smartWebView.getWebView();
        smartWebView.getWebView().getSettings().setSupportZoom(true);
        smartWebView.getWebView().getSettings().setDisplayZoomControls(true);
        smartWebView.getWebView().getSettings().setBuiltInZoomControls(true);
        // 必须
        smartWebView.getWebView().getSettings().setJavaScriptEnabled(true);
        smartWebView.getWebView().getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//        smartWebView.loadUrl("https://player.bilibili.com/player.html?aid=582731140&bvid=BV1S64y1u7bL&cid=174633886&page=1");

        String htmlStr="<iframe src=\"//player.bilibili.com/player.html?bvid=BV1S64y1u7bL&cid=174633886&page=1\" scrolling=\"no\" border=\"0\" frameborder=\"no\" framespacing=\"0\"></iframe>";

        htmlStr = htmlStr.replaceAll("iframe\\s+src\\s*=\\s*\"//","iframe src=\"https://");
//        webView.loadData(htmlStr, "text/html; charset=UTF-8", null);

        smartWebView.loadUrl("https://www.bilibili.com/video/BV1S64y1u7bL");
    }

    @Override
    public void onClick(View v) {

    }
}
