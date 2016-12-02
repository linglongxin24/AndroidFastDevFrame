package cn.bluemobi.dylan.fastdev.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import cn.bluemobi.dylan.fastdev.utils.Tools;

/**
 * 下拉选择对话框
 * Created by yuandl on 2016-12-02.
 */

public class SelectPopupWindow extends PopupWindow {
    private Context context;
    private View showAsDropDownView;
    private FrameLayout outsideFrameLayout;
    private ListAdapter listAdapter;
    private PopupWindow popupWindow;
    private ListView listView;

    /**
     * 初始化一个选择下拉框
     *
     * @param showAsDropDownView 要在哪个View下面展示
     * @param listAdapter        下拉列表数据适配器
     * @param outsideFrameLayout 外部含有遮罩层的FrameLayout
     */
    public SelectPopupWindow(View showAsDropDownView, ListAdapter listAdapter, FrameLayout outsideFrameLayout) {
        this.showAsDropDownView = showAsDropDownView;
        this.outsideFrameLayout = outsideFrameLayout;
        this.context = showAsDropDownView.getContext();
        this.listAdapter = listAdapter;
        init();
    }

    /**
     * 初始化PopupWindow
     */
    private void init() {
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listView = new ListView(context);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listView.setDivider(new ColorDrawable(context.getResources().getColor(android.R.color.darker_gray)));
        listView.setDividerHeight(Tools.DPtoPX(1, context));
        listView.setBackgroundColor(Color.WHITE);
        listView.setAdapter(listAdapter);
        popupWindow.setContentView(listView);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (outsideFrameLayout != null) {
                    outsideFrameLayout.setForeground(new ColorDrawable(Color.TRANSPARENT));
                }
            }
        });

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
            if (outsideFrameLayout != null) {
                outsideFrameLayout.setForeground(new ColorDrawable(Color.parseColor("#e05f5f5f")));
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
            init();
        }
        if (listView == null) {
            return this;
        }
        if (onItemClickListener != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popupWindow.dismiss();
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
            });
        }
        return this;
    }
}
