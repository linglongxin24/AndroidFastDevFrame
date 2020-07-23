package cn.bluemobi.dylan.photoview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片放大缩小浏览工具类
 *
 * @author YDL
 */
public class PhotoScaleUtils {
    /**
     * 浏览单张图片
     *
     * @param mContext 上下文
     * @param url      图片地址
     */
    public static void scale(Context mContext, String url) {
        List<String> list = new ArrayList<String>();
        list.add(url);
        scale(mContext, list);
    }
    /**
     * 浏览单张图片
     *
     * @param mContext 上下文
     * @param url      图片地址
     */
    public static void scale(Context mContext, String url, boolean isShowSaveButton) {
        List<String> list = new ArrayList<String>();
        list.add(url);
        scale(mContext, list, 0,isShowSaveButton);
    }

    /**
     * 浏览多张图片
     *
     * @param mContext 上下文
     * @param paths    图片地址列表
     */
    public static void scale(Context mContext, List<String> paths) {
        scale(mContext, paths, 0,false);
    }


    /**
     * 浏览多张图片
     *
     * @param mContext 上下文
     * @param paths    图片地址列表
     */
    public static void scale(Context mContext, List<String> paths, boolean isShowSaveButton) {
        scale(mContext, paths, 0);
    }
    /**
     * 浏览多张图片的指定位置图片
     *
     * @param mContext 上下文
     * @param paths    图片地址集合
     * @param position 位置
     */
    public static void scale(Context mContext, List<String> paths, int position) {
        scale(mContext, paths, position, false);
    }

    /**
     * 浏览多张图片的指定位置图片
     *
     * @param mContext 上下文
     * @param paths    图片地址集合
     * @param position 位置
     */
    public static void scale(Context mContext, List<String> paths, int position, boolean isShowSaveButton) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取

        String[] arr = new String[paths.size()];
        arr = paths.toArray(arr);
        intent.putExtra(ImagePagerActivity.EXTRA_IS_SHOW_SAVE_BUTTON, isShowSaveButton);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, arr);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
        if (mContext instanceof Activity) {
            //切换Activity的过渡动
            ((Activity) mContext).overridePendingTransition(R.anim.zoom_ente, R.anim.zoom_exit);
        }
    }
}
