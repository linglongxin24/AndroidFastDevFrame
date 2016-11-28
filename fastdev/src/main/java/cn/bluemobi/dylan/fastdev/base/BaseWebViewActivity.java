package cn.bluemobi.dylan.fastdev.base;

import android.content.Context;
import android.os.Build;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by yuandl on 2016/9/21 0021.
 */

public abstract class BaseWebViewActivity extends BaseActivity {
    /**
     * 加载网页
     *
     * @param wv
     * @param url
     */
    protected void loadUrl(WebView wv, String url) {
        if (Build.VERSION.SDK_INT >= 19) {
            wv.getSettings().setLoadsImagesAutomatically(true);
        } else {
            wv.getSettings().setLoadsImagesAutomatically(false);
        }
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        wv.getSettings().setUseWideViewPort(true);
//        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.setVerticalScrollBarEnabled(false);
        wv.setVerticalScrollbarOverlay(false);
        wv.setHorizontalScrollBarEnabled(false);
        wv.setHorizontalScrollbarOverlay(false);
        WebSettings webSettings = wv.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setScale(wv);
//        setDefaultZoom();
//        //让缩放显示的最小值为起始
//        wv.setInitialScale(3);
        // 设置支持缩放
        webSettings.setSupportZoom(false);
        // 设置缩放工具的显示
        webSettings.setBuiltInZoomControls(false);
        wv.setWebViewClient(new MyWebViewClient(wv));
        //加载需要显示的网页
        wv.loadUrl(url);
    }

    private class MyWebViewClient extends WebViewClient {
        private WebView wv;

        public MyWebViewClient(WebView wv) {
            this.wv = wv;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!wv.getSettings().getLoadsImagesAutomatically()) {
                wv.getSettings().setLoadsImagesAutomatically(true);
            }
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
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
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

}
