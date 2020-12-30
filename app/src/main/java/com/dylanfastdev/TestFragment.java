package com.dylanfastdev;

import android.Manifest;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bjtsn.dylan.lifecycleobserver.LifecycleCallback;
import com.bjtsn.dylan.lifecycleobserver.LifecycleObserver;
import com.orhanobut.logger.Logger;

import java.util.Objects;

import cn.bluemobi.dylan.base.BaseFragment;
import cn.bluemobi.dylan.http.dialog.LoadingDialog;

/**
 * @author dylan
 * @date 2019/11/30
 */
public class TestFragment extends BaseFragment {
    private ImageView iv;

    @Override
    protected int setContentView() {
        return R.layout.customview;
    }

    @Override
    protected void initView(View view) {
        iv = view.findViewById(R.id.iv);
    }

    @Override
    protected void initData() {
        new LoadingDialog(mContext).show("测试生命周期");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(getActivity()).finish();
            }
        },2000);
        new LifecycleObserver(this).observer(new LifecycleCallback() {
            @Override
            public void onCreate() {
                Logger.d(TestFragment.this.getClass().getSimpleName() + "onCreate");
            }

            @Override
            public void onStart() {
                Logger.d(TestFragment.this.getClass().getSimpleName() + "onStart");
            }

            @Override
            public void onResume() {
                Logger.d(TestFragment.this.getClass().getSimpleName() + "onResume");
            }

            @Override
            public void onPause() {
                Logger.d(TestFragment.this.getClass().getSimpleName() + "onPause");
            }

            @Override
            public void onStop() {
                Logger.d(TestFragment.this.getClass().getSimpleName() + "onStop");
            }

            @Override
            public void onDestroy() {
                Logger.d(TestFragment.this.getClass().getSimpleName() + "onDestroy");
            }
        });
//        Http.with(mContext)
//                .setObservable(
//                        Http.getApiService(ApiService.class)
//                                .test("13284", 10, 1))
//                .setDataListener(new HttpCallBack() {
//                    @Override
//                    public void netOnSuccess(Map<String, Object> data) {
////                      List<Map<String,Object>> list= JsonParse.getList(data, "data");
////                      List<String> s= JsonParse.getList(data, "data");
////                        startActivity(new Intent(mContext,MainActivity.class));
////                        finish();
//                    }
//
//                    @Override
//                    public void netOnFinish() {
//                        super.netOnFinish();
//                    }
//                });
//        Glide.with(mContext).load("https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=180092f702f431adbcd2443f730dcb92/f636afc379310a5519aea991b94543a9832610c8.jpg")
//                .into(iv);
//        new Handler().postDelayed(() -> getActivity().finish(),2*1000);


        String[] PERMISSIONS = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
        };
//        LifecycleDetector.getInstance().observer(getActivity(), new LifecycleListener() {
//
//            @Override
//            public void onStart() {
//                Logger.d("LifecycleDetector---onStart");
//            }
//
//            @Override
//            public void onStop() {
//                Logger.d("LifecycleDetector---onStop");
//            }
//
//            @Override
//            public void onDestroy() {
//                Logger.d("LifecycleDetector---onDestroy");
//            }
//
//            @Override
//            public void onResume() {
//                Logger.d("LifecycleDetector---onResume");
//            }
//        });
    }

    @Override
    public void addListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
