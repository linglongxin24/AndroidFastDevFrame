package com.dylanfastdev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bjtsn.dylan.lifecycleobserver.LifecycleCallback;
import com.bjtsn.dylan.lifecycleobserver.LifecycleObserver;
import com.bjtsn.dylan.startactivityforresult.StartActivityForResult;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.Nullable;

import cn.bluemobi.dylan.base.BaseActivity;

/**
 * @author YDL
 * @version 1.0
 * @date 2020/12/29/14:48
 */
public class StartActivityForResultActivity extends BaseActivity {
    @Override
    public void initTitleBar() {
        setTitle("请求跳转界面");
    }

    @Override
    protected int getContentView() {
        return R.layout.ac_start_activity_for_result;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        findViewById(R.id.bt_start_activity_for_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new RequestPermission(mActivity).requestPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .setPermissionCheckCallBack(new RequestPermission.PermissionCheckCallBack() {
//                            @Override
//                            public void onSucceed() {
//                                Toast.makeText(mContext, "已同意权限", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onReject(String[] strings) {
//                                Toast.makeText(mContext, "拒绝权限" + Arrays.toString(strings), Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onRejectAndNoAsk(String[] strings) {
//                                Toast.makeText(mContext, "拒绝不再询问权限" + Arrays.toString(strings), Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
                new StartActivityForResult(mActivity).startActivityForResult(new Intent(mContext, ReturnResultActivity.class))
                        .setOnActivityResultCallBack(new StartActivityForResult.CallBack() {
                            @Override
                            public void onActivityResult(int resultCode, @Nullable Intent data) {
                                if (data != null) {
                                    Bundle extras = data.getExtras();
                                    for (String s : extras.keySet()) {
                                        Logger.d(s + "=" + extras.get(s));
                                    }
                                }
                                Toast.makeText(mContext, "resultCode=" + resultCode, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
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
    }

    @Override
    public void addListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
