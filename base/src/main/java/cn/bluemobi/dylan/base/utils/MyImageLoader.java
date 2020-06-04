package cn.bluemobi.dylan.base.utils;

import android.content.Context;
import androidx.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;


/**
 * @author dylan
 * @date 2019-06-14
 */
public class MyImageLoader {
    public static void loadImage(Context mContext, String url, ImageView imageView) {
        Glide.with(mContext).load(url).into(imageView);
    }

    public static void loadImage(Context mContext, String url, ImageView imageView, @DrawableRes int placeholder, @DrawableRes int error) {
        Glide.with(mContext).load(url).placeholder(placeholder).error(error).into(imageView);
    }

    public static void loadRoundImage(Context mContext, String url, ImageView imageView) {
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                //不做磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //不做内存缓存
                .skipMemoryCache(true);
        Glide.with(mContext)
                .load(url)
                .apply(mRequestOptions)
                .into(imageView);
    }

    public static void loadRoundImage(Context mContext, String url, ImageView imageView, @DrawableRes int placeholder, @DrawableRes int error) {
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                //设置等待时的图片
                .placeholder(placeholder)
                //设置加载失败后的图片显示
                .error(error)
                //不做磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //不做内存缓存
                .skipMemoryCache(true);
        Glide.with(mContext)
                .load(url)
                .apply(mRequestOptions)
                .into(imageView);

    }

    public static void loadRoundCornerImage(Context mContext, String url, ImageView imageView, int roundingRadius) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(6);
        RequestOptions mRequestOptions = RequestOptions.bitmapTransform(roundedCorners)
                //不做磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //不做内存缓存
                .skipMemoryCache(true);
        Glide.with(mContext)
                .load(url)
                .apply(mRequestOptions)
                .into(imageView);
    }

    public static void loadRoundCornerImage(Context mContext, String url, ImageView imageView, @DrawableRes int placeholder, @DrawableRes int error, int roundingRadius) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(6);
        RequestOptions mRequestOptions = RequestOptions.bitmapTransform(roundedCorners)
                //设置等待时的图片
                .placeholder(placeholder)
                //设置加载失败后的图片显示
                .error(error)
                //不做磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //不做内存缓存
                .skipMemoryCache(true);
        Glide.with(mContext)
                .load(url)
                .apply(mRequestOptions)
                .into(imageView);

    }
}
