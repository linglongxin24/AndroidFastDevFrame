package cn.bluemobi.dylan.base;

/**
 *
 * @author YDL
 * @date 2017/4/18
 */

public class AppConfig {
    private static Integer appLeftResId;

    public static void setAppLeftResId(Integer leftResId) {
        appLeftResId = leftResId;
    }

    public static Integer getAppLeftResId() {
        return appLeftResId;
    }
}
