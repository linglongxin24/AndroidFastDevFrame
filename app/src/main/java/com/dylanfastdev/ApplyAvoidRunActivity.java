package com.dylanfastdev;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bluemobi.dylan.base.adapter.GridViewAddImagesAdapter;
import cn.bluemobi.dylan.base.adapter.common.abslistview.CommonAdapter;
import cn.bluemobi.dylan.base.utils.Base64;
import cn.bluemobi.dylan.base.utils.Tools;
import cn.bluemobi.dylan.base.view.MyGridView;
import cn.bluemobi.dylan.base.*;

/**
 * @author lenovo
 * @date 2017/7/15
 */

public class ApplyAvoidRunActivity extends BaseActivity {
    private EditText et_content;
    private MyGridView gv_images;
    private Button bt_commit;
    private Spinner spinner;


    @Override
    public void initTitleBar() {
        setTitle("申请免跑");
    }

    @Override
    protected int getContentView() {
        return R.layout.ac_apply_mc;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        et_content = (EditText) findViewById(R.id.et_content);
        gv_images = (MyGridView) findViewById(R.id.gv_images);
        bt_commit = (Button) findViewById(R.id.bt_commit);
        spinner = (Spinner) findViewById(R.id.spinner);
    }

    private List<String> paths = new ArrayList<>();

    @Override
    public void initData() {
        GridViewAddImagesAdapter gridViewAddImagesAdapter = new GridViewAddImagesAdapter(mActivity, 4, Tools.DPtoPX(10, mContext));
        gridViewAddImagesAdapter.setMaxImages(4);
        gv_images.setAdapter(gridViewAddImagesAdapter);
    }

    @Override
    public void addListener() {
    }

    @Override
    public void onClick(View v) {
    }


}
