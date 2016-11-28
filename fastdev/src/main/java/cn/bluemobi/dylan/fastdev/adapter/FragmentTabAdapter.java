package cn.bluemobi.dylan.fastdev.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.util.List;

/**
 * fragment切换适配器
 */
public class FragmentTabAdapter implements OnCheckedChangeListener {

    private List<Fragment> fragments; // 一个tab页面对应一个Fragment
    private RadioGroup rg; // 用于切换tab
    private FragmentActivity fragmentActivity; // Fragment所属的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id

    private int currentTab = 0;// 当前Tab页面索引

    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能

    public FragmentTabAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId, RadioGroup rg) {
        this.fragments = fragments;
        this.rg = rg;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        ft.add(fragmentContentId, fragments.get(currentTab), String.valueOf(currentTab));
        ft.commitAllowingStateLoss();
        if (rg != null) {
            rg.setOnCheckedChangeListener(this);
        }


    }

    private boolean isLogin = true;

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if(!isLogin){
            ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
            // 如果设置了切换tab额外功能功能接口
            if (null != onRgsExtraCheckedChangedListener) {
                onRgsExtraCheckedChangedListener.needLogin();
            }
            return;
        }

        try {
            for (int i = 0; i < rg.getChildCount(); i++) {
                if (rg.getChildAt(i).getId() == checkedId) {
                    Fragment fragment = fragments.get(i);
                    FragmentTransaction ft = obtainFragmentTransaction(i);
                    //             getCurrentFragment().onPause(); // 暂停当前tab
                    //getCurrentFragment().onStop(); // 暂停当前tab

                    if (fragment.isAdded()) {
                        // fragment.onStart(); // 启动目标tab的onStart()
                        currentTab = i;
                        if (!fragment.isResumed()) {

                            fragment.onResume(); // 启动目标tab的onResume()
                        }
                    } else {
                        ft.add(fragmentContentId, fragment, String.valueOf(currentTab));
                    }
                    showTab(i); // 显示目标tab
                    ft.commitAllowingStateLoss();

                    // 如果设置了切换tab额外功能功能接口
                    if (null != onRgsExtraCheckedChangedListener) {
                        onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(
                                radioGroup, checkedId, i);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 切换tab
     *
     * @param idx
     */
    public void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);

            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commitAllowingStateLoss();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    /**
     * 获取一个带动画的FragmentTransaction
     *
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();

        return ft;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
        return onRgsExtraCheckedChangedListener;
    }

    public void setOnRgsExtraCheckedChangedListener(
            OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    /**
     * 切换tab额外功能功能接口
     */
    public static class OnRgsExtraCheckedChangedListener {
        public void needLogin(){
        }
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,
                                             int checkedId, int index) {

        }
    }

}
