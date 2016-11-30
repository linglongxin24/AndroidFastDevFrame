#AndroidFastDevFrame

#1.需要在Application中初始化以下配置

```java
package cn.bluemobi.dylan.fastdev.config;


import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.orhanobut.logger.Logger;

import org.xutils.x;

import java.util.Map;

import cn.bluemobi.dylan.fastdev.http.HttpUtils;

/**
 * 配置类
 * Created by yuandl on 2016/9/1 0001.
 */

public class Config {
    /**
     * 初始化配置
     *
     * @param application 应用的application
     */
    public static void init(Application application) {
        x.Ext.init(application);
    }

    /**
     * 初始化日志TAG
     *
     * @param tag 日志TAG
     */
    public static void initLog(String tag) {
        Logger.init(tag);
    }

    /**
     * 初始化存放文件的路径
     *
     * @param path 存放文件的路径
     */
    public static void initFilePath(String path) {
        FilePath.IMAGE_DIR = path;
    }

    /**
     * 初始化标题栏
     *
     * @param backgroundColor 背景颜色id
     * @param textColor       字体颜色id
     * @param arrowBack       返回箭头图标id
     */
    public static void initTitleBar(int backgroundColor, int textColor, int arrowBack) {
        TitleBarColor.backgroundColor = backgroundColor;
        TitleBarColor.textColor = textColor;
        TitleBarColor.arrowBack = arrowBack;
    }

    /**
     * 初始化网络请求的各种参数
     *
     * @param code             请求接口返回码
     * @param data             请求接口返回数据
     * @param msg              请求接口返回信息
     * @param successCode      请求接口成功的响应码
     * @param globalParameters 配置请求接口的全局参数
     */
    public static void initHttp(String code, String data, String msg, int successCode, Map<String, String> globalParameters) {
        HttpUtils.getInstance().init(code, data, msg, successCode, globalParameters);
    }

}

```

#2.轮播图用法

```java
  cycle_view_pager = (CycleViewPager) findViewById(R.id.cv);
        //设置选中和未选中时的图片
        assert cycle_view_pager != null;
        cycle_view_pager.setIndicators(R.mipmap.dot_focus, R.mipmap.dot_normal);
        List<String> urls = new ArrayList<>();
        urls.add("http://pic.58pic.com/58pic/15/37/32/458PICq58PICPd4_1024.jpg");
        urls.add("http://pic8.nipic.com/20100627/1614097_004848992811_2.jpg");
        urls.add("http://pic.58pic.com/58pic/15/35/53/14t58PICUJv_1024.jpg");
        cycle_view_pager.setData(urls, new CycleViewPager.ImageCycleViewListener() {
            @Override
            public void onImageClick(String url, int position, View imageView) {

            }
        });
```
