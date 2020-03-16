package cn.bluemobi.dylan.base.utils.activityresult;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class ActResultRequest {

    private OnActResultEventDispatcherFragment fragment;

    public ActResultRequest(FragmentActivity activity) {
        fragment = getEventDispatchFragment(activity);
    }

    public ActResultRequest(Fragment fragment) {
        this.fragment = getEventDispatchFragment(fragment);
    }

    private OnActResultEventDispatcherFragment getEventDispatchFragment(FragmentActivity activity) {
        final FragmentManager fragmentManager =activity.getSupportFragmentManager();
        OnActResultEventDispatcherFragment fragment = findEventDispatchFragment(fragmentManager);
        if (fragment == null) {
            fragment = new OnActResultEventDispatcherFragment();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, OnActResultEventDispatcherFragment.TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    private OnActResultEventDispatcherFragment getEventDispatchFragment(Fragment fragmentOutside) {
        final FragmentManager fragmentManager = fragmentOutside.getChildFragmentManager();
        OnActResultEventDispatcherFragment fragment = findEventDispatchFragment(fragmentManager);
        if (fragment == null) {
            fragment = new OnActResultEventDispatcherFragment();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, OnActResultEventDispatcherFragment.TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }
    private OnActResultEventDispatcherFragment findEventDispatchFragment(FragmentManager manager) {
        return (OnActResultEventDispatcherFragment) manager.findFragmentByTag(OnActResultEventDispatcherFragment.TAG);
    }

    public void startForResult(Intent intent, Callback callback) {
        fragment.startForResult(intent, callback);
    }

    public interface Callback {

        void onActivityResult(int resultCode, Intent data);
    }
}