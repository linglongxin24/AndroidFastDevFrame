package cn.bluemobi.dylan.fastdev.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.orhanobut.logger.Logger;

import cn.bluemobi.dylan.fastdev.utils.Tools;

/**
 * 下拉选择对话框
 * Created by yuandl on 2016-12-02.
 */

public class SelectPopupWindow extends PopupWindow {
    private Activity activity;
    private Context context;
    private View showAsDropDownView;
    private ListAdapter listAdapter;
    private PopupWindow popupWindow;
    private ListView listView;
    private FrameLayout outsideView;

    /**
     * 初始化一个选择下拉框
     *
     * @param showAsDropDownView 要在哪个View下面展示
     * @param listAdapter        下拉列表数据适配器
     * @param outsideView        外部含有遮罩层的FrameLayout
     */
    public SelectPopupWindow(Activity activity,View showAsDropDownView, ListAdapter listAdapter, FrameLayout outsideView) {
        this.activity = activity;
        this.showAsDropDownView = showAsDropDownView;
        this.context = showAsDropDownView.getContext();
        this.listAdapter = listAdapter;
        this.outsideView = outsideView;
        init();
    }

    /**
     * 初始化PopupWindow
     */
    private void init() {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, height/2);
        listView = new ListView(context);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listView.setDivider(new ColorDrawable(context.getResources().getColor(android.R.color.darker_gray)));
        listView.setDividerHeight(Tools.DPtoPX(1, context));
        listView.setBackgroundColor(Color.WHITE);
        listView.setAdapter(listAdapter);
        setOnItemClickListener(null);
        popupWindow.setContentView(listView);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {

                if (outsideView != null) {
                    outsideView.setAlpha(1f);
//                    outsideView.setForeground(new ColorDrawable(Color.TRANSPARENT));

//
//                    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    activity.getWindow().setAttributes(lp);
                }
            }
        });

    }

    /**
     * 获取显示布局的listview
     *
     * @return
     */
    public ListView getListView() {
        return listView;
    }

    /**
     * 显示PopupWindow
     */
    public SelectPopupWindow show() {
        if (popupWindow == null) {
            init();
        }
        if (!popupWindow.isShowing()) {
            popupWindow.showAsDropDown(showAsDropDownView);

            // 设置背景颜色变暗
//            WindowManager.LayoutParams lp = activity. getWindow().getAttributes();
//            lp.alpha = 0.7f;
//            activity.getWindow().setAttributes(lp);
            outsideView.setAlpha(0.5f);
            if (outsideView != null) {
                outsideView.setForeground(new ColorDrawable(Color.parseColor("#00B1B1B1")));
            }
        }
        return this;
    }

    /**
     * 设置下拉选择框的item点击事件
     *
     * @param onItemClickListener
     */
    public SelectPopupWindow setOnItemClickListener(final AdapterView.OnItemClickListener onItemClickListener) {
        if (popupWindow == null) {
            return this;
        }
        if (listView == null) {
            return this;
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
        return this;
    }
}
