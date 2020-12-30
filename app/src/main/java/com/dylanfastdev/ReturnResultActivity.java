package com.dylanfastdev;


import android.os.Bundle;
import android.view.View;

import com.bjtsn.dylan.lifecycleobserver.LifecycleCallback;
import com.bjtsn.dylan.lifecycleobserver.LifecycleObserver;
import com.orhanobut.logger.Logger;

import cn.bluemobi.dylan.base.BaseActivity;

/**
 * @author YDL
 * @version 1.0
 * @date 2020/12/29/14:49
 */
public class ReturnResultActivity extends BaseActivity {
    @Override
    public void initTitleBar() {
        setTitle("请求跳转回传界面");
    }

    @Override
    protected int getContentView() {
        return R.layout.ac_return_result;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        findViewById(R.id.button).setOnClickListener(v -> {
//            Intent intent = new Intent();
//            intent.putExtra("code", 1001);
//            intent.putExtra("msg", "回传成功");
//            setResult(RESULT_OK,intent);
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public void initData() {
        new LifecycleObserver(this).observer(new LifecycleCallback() {
            @Override
            public void onCreate() {
                Logger.d(mActivity.getClass().getSimpleName()+"onCreate");
            }

            @Override
            public void onStart() {
                Logger.d(mActivity.getClass().getSimpleName()+"onStart");
            }

            @Override
            public void onResume() {
                Logger.d(mActivity.getClass().getSimpleName()+"onResume");
            }

            @Override
            public void onPause() {
                Logger.d(mActivity.getClass().getSimpleName()+"onPause");
            }

            @Override
            public void onStop() {
                Logger.d(mActivity.getClass().getSimpleName()+"onStop");
            }

            @Override
            public void onDestroy() {
                Logger.d(mActivity.getClass().getSimpleName()+"onDestroy");
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.ll_root,new TestFragment()).commitAllowingStateLoss();
    }

    @Override
    public void addListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
