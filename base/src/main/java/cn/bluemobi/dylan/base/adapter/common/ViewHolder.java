package cn.bluemobi.dylan.base.adapter.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.bluemobi.dylan.base.utils.MyImageLoader;


public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;
    private int mLayoutId;
    private int myPosition=getLayoutPosition();

    public int getMyPosition() {
        return myPosition;
    }

    public ViewHolder setMyPosition(int myPosition) {
        this.myPosition = myPosition;
        return this;
    }

    public ViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
        mConvertView.setTag(this);

    }


    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId) {
        if (convertView == null) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                    false);
            ViewHolder holder = new ViewHolder(context, itemView, parent);
            holder.mLayoutId = layoutId;
            return holder;
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            return holder;
        }
    }


    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置View是否可用
     *
     * @param viewId
     * @return
     */
    public ViewHolder setEnabled(int viewId, boolean enabled) {
        View tv = getView(viewId);
        tv.setEnabled(enabled);
        return this;
    }

    /**
     * 获取View是否可用
     *
     * @param viewId
     * @return
     */
    public boolean getEnabled(int viewId) {
        View tv = getView(viewId);
        return tv.isEnabled();
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @return
     */
    public String getText(int viewId) {
        TextView tv = getView(viewId);
        return tv.getText().toString().trim();
    }


    /**
     * 设置TextView的Hint值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setHint(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setHint(text);
        return this;
    }

    /**
     * 获取TextView的Hint值
     *
     * @param viewId
     * @return
     */
    public String getHint(int viewId) {
        TextView tv = getView(viewId);
        return tv.getHint().toString().trim();
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setImageUrl(int viewId, String url) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(url).into(view);
        return this;
    }

    public ViewHolder setImageUrl(int viewId, String url, int emptyResourceId, int errorResourceId) {
        ImageView view = getView(viewId);
        Glide.with(mContext).load(url).placeholder(emptyResourceId).error(errorResourceId).into(view);
        return this;
    }

    public ViewHolder setRoundImageUrl(int viewId, String url, int emptyResourceId, int errorResourceId) {
        ImageView view = getView(viewId);
        MyImageLoader.loadRoundImage(mContext, url, view, emptyResourceId, errorResourceId);
        return this;
    }

    public ViewHolder setRoundImageUrl(int viewId, String url) {
        ImageView view = getView(viewId);
        MyImageLoader.loadRoundImage(mContext, url, view);
        return this;
    }

    public ViewHolder setRoundCornerImageUrl(int viewId, String url, int cornerRadius, int emptyResourceId, int errorResourceId) {
        ImageView view = getView(viewId);
        MyImageLoader.loadRoundCornerImage(mContext, url, view, emptyResourceId, errorResourceId, cornerRadius);
        return this;
    }

    public ViewHolder setRoundCornerImageUrl(int viewId, String url, int cornerRadius) {
        ImageView view = getView(viewId);
        MyImageLoader.loadRoundCornerImage(mContext, url, view, cornerRadius);
        return this;
    }

    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public ViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 关于事件的
     */
    public ViewHolder setOnClickListener(int viewId,
                                         View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(int viewId,
                                         View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId,
                                             View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public int getLayoutId() {
        return mLayoutId;
    }
}
