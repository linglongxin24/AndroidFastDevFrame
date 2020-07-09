package cn.bluemobi.dylan.base.utils.activitypermission;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

;

public class ActPermissionRequest {

    private OnActPermissionEventDispatcherFragment fragment;

    public ActPermissionRequest(FragmentActivity activity) {
        fragment = getEventDispatchFragment(activity);
    }

    public ActPermissionRequest(Fragment fragment) {
        this.fragment = getEventDispatchFragment(fragment);
    }

    private OnActPermissionEventDispatcherFragment getEventDispatchFragment(FragmentActivity activity) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        OnActPermissionEventDispatcherFragment fragment = findEventDispatchFragment(fragmentManager);
        if (fragment == null) {
            fragment = new OnActPermissionEventDispatcherFragment();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, OnActPermissionEventDispatcherFragment.TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    private OnActPermissionEventDispatcherFragment getEventDispatchFragment(Fragment fragmentOutside) {
        final FragmentManager fragmentManager = fragmentOutside.getChildFragmentManager();
        OnActPermissionEventDispatcherFragment fragment = findEventDispatchFragment(fragmentManager);
        if (fragment == null) {
            fragment = new OnActPermissionEventDispatcherFragment();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, OnActPermissionEventDispatcherFragment.TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    private OnActPermissionEventDispatcherFragment findEventDispatchFragment(FragmentManager manager) {
        return (OnActPermissionEventDispatcherFragment) manager.findFragmentByTag(OnActPermissionEventDispatcherFragment.TAG);
    }

    public void requestPermission(String permission, PermissionCheckCallBack callback) {
        requestPermission(new String[]{permission}, callback);
    }

    public void requestPermission(String[] permissions, PermissionCheckCallBack callback) {
        requestPermission(permissions).setPermissionCheckCallBack(callback);
    }

    private String[] permissions;

    public ActPermissionRequest requestPermission(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public void setPermissionCheckCallBack(PermissionCheckCallBack callback) {
        if (callback != null) {
            fragment.requestPermissions(permissions, callback);
        }
    }

    public interface PermissionCheckCallBack {

        /**
         * 用户已授予权限
         */
        void onSucceed();

        /**
         * 用户已拒绝过权限
         *
         * @param permission:被拒绝的权限
         */
        void onReject(String... permission);

        /**
         * 用户已拒绝过并且已勾选不再询问选项、用户第一次申请权限;
         *
         * @param permission:被拒绝的权限
         */
        void onRejectAndNoAsk(String... permission);
    }
}