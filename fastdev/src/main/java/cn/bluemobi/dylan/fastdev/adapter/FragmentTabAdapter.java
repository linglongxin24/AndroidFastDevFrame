package cn.bluemobi.dylan.fastdev.adapter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * fragment切换适配器
 */
public class FragmentTabAdapter implements TabLayout.OnTabSelectedListener {

    private List<Fragment> fragments; // 一个tab页面对应一个Fragment
    private TabLayout tabLayout; // 用于切换tab
    private FragmentManager fragmentManager; // Fragment所属的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id

    private int currentTab = 0;// 当前Tab页面索引

    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能

    public FragmentTabAdapter(FragmentManager fragmentManager, List<Fragment> fragments, int fragmentContentId, TabLayout tabLayout) {
        this.fragments = fragments;
        this.tabLayout = tabLayout;
        this.fragmentManager = fragmentManager;
        this.fragmentContentId = fragmentContentId;

        // 默认显示第一页
        FragmentTransaction ft = fragmentManager
                .beginTransaction();
        ft.add(fragmentContentId, fragments.get(currentTab), String.valueOf(currentTab));
        ft.commitAllowingStateLoss();
        if (tabLayout != null) {
            tabLayout.setOnTabSelectedListener(this);
        }

    }

    private boolean isLogin = true;

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
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
        FragmentTransaction ft =fragmentManager
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Logger.d("tab=" + tab.getPosition());
        if (!isLogin) {
            // 如果设置了切换tab额外功能功能接口
            if (null != onRgsExtraCheckedChangedListener) {
                onRgsExtraCheckedChangedListener.needLogin();
            }
            return;
        }

        try {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                if (tabLayout.getTabAt(i) == tab) {
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
                                tabLayout, tab, i);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * 切换tab额外功能功能接口
     */
    public static class OnRgsExtraCheckedChangedListener {
        public void needLogin() {
        }

        public void OnRgsExtraCheckedChanged(TabLayout tabLayout,
                                             TabLayout.Tab tab, int index) {

        }
    }

}
