package cn.bluemobi.dylan.fastdev.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by liangqs on 2016/9/7.
 * 沉浸式状态栏
 */
public class ImmersionUtil {
    private Activity mActivity=null;

    public ImmersionUtil(Activity activity) {
        this.mActivity=activity;
    }

    /**
     * 初始化设置沉浸式状态栏
     */
    public void initStatusBar() {
        Window win = mActivity.getWindow();

        //4.4版本以上才有沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //透明状态栏
            win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            //解决5.0版本以上沉浸式状态栏为半透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 设置系统UI布局全屏
                win.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

                //设置状态栏全透明，先清除透明状态栏设置。
                win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //直接设置状态栏的颜色。
                win.setStatusBarColor(Color.TRANSPARENT);
            }
        }

    }


}
