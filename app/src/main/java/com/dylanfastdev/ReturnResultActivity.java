package com.dylanfastdev;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    }

    @Override
    public void addListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
