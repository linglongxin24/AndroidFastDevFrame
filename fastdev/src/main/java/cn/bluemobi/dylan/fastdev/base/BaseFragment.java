package cn.bluemobi.dylan.fastdev.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

import cn.bluemobi.dylan.fastdev.http.HttpResponse;
import cn.bluemobi.dylan.fastdev.http.HttpUtils;

/**
 * com.bm.falvzixun.fragment.BaseFragment
 *
 * @author yuandl on 2016/1/15.
 *         描述主要干什么
 */
public class BaseFragment extends Fragment implements HttpResponse {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    private Callback.Cancelable cancelable;


    /**
     * 异步网络请求类
     *
     * @param requestParams
     */
    protected void ajax(RequestParams requestParams) {
        ajax(requestParams, -1, true);
    }

    /**
     * 异步网络请求类
     *
     * @param requestParams
     */
    protected void ajax(RequestParams requestParams, boolean isShowLoadingDialog) {
        ajax(requestParams, -1, isShowLoadingDialog);
    }

    /**
     * 异步网络请求类
     *
     * @param requestParams
     * @param requestCode   区分不同的网络请求
     */
    protected void ajax(RequestParams requestParams, int requestCode) {
        ajax(requestParams, requestCode, true);
    }

    /**
     * 异步网络请求类
     *
     * @param requestParams
     * @param requestCode   区分不同的网络请求
     */
    protected void ajax(RequestParams requestParams, int requestCode, boolean isShowLoadingDialog) {
        cancelable = HttpUtils.getInstance().ajax(getContext(), requestParams, requestCode, isShowLoadingDialog, this);
    }

    private Toast toast;

    /**
     * 弹出Toast便捷方法
     *
     * @param charSequence
     */
    public void showToast(CharSequence charSequence) {
        if (null == toast) {
            toast = Toast.makeText(x.app(), charSequence, Toast.LENGTH_SHORT);
        } else {
            toast.setText(charSequence);
        }
        toast.show();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != toast) {
            toast.cancel();
        }

    }

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    protected boolean isLoadData = false;
    protected boolean isPrepared = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected void lazyLoad() {

    }

    protected void setEmptyView(ListView listView) {
        TextView emptyView = new TextView(getContext());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暂无数据！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        listView.setVisibility(View.VISIBLE);
    }

    protected void setEmptyView(ListView listView, String text) {
        TextView emptyView = new TextView(getContext());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText(text);
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
    }

    protected void setEmptyView(GridView gridView) {
        TextView emptyView = new TextView(getContext());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暂无数据！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) gridView.getParent()).addView(emptyView);
        gridView.setEmptyView(emptyView);
    }

    protected void setEmptyView(GridView gridView, String text) {
        TextView emptyView = new TextView(getContext());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText(text);
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) gridView.getParent()).addView(emptyView);
        gridView.setEmptyView(emptyView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cancelable != null && !cancelable.isCancelled()) {
            cancelable.cancel();
        }
//        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }

    @Override
    public void netOnStart() {

    }

    @Override
    public void netOnStart(int requestCode) {

    }

    @Override
    public void netOnSuccess(Map<String, Object> data, int requestCode) {

    }

    @Override
    public void netOnSuccess(Map<String, Object> data) {

    }

    @Override
    public void netOnOtherStatus(int status, String msg, int requestCode) {

    }

    @Override
    public void netOnOtherStatus(int status, String msg) {

    }

    @Override
    public void netOnFinish() {

    }

    @Override
    public void netOnFinish(int requestCode) {

    }

    @Override
    public void netOnFailure(Throwable ex) {

    }

    @Override
    public void netOnFailure(int requestCode, Throwable ex) {

    }
}
