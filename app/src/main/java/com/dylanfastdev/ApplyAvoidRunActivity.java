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

import cn.bluemobi.dylan.base.BaseActivity;
import cn.bluemobi.dylan.base.adapter.GridViewAddImagesAdapter;
import cn.bluemobi.dylan.base.adapter.common.abslistview.CommonAdapter;
import cn.bluemobi.dylan.base.utils.Base64;
import cn.bluemobi.dylan.base.utils.Tools;
import cn.bluemobi.dylan.base.view.MyGridView;

/**
 * @author lenovo
 * @date 2017/7/15
 */

public class ApplyAvoidRunActivity extends BaseActivity {
    private EditText et_content;
    private MyGridView gv_images;
    private Button bt_commit;
    private List<String> paths = new ArrayList<>();
    /**
     * 拍照和从手机相册选择的对话框
     */
    private Dialog dialog;
    /**
     * 拍照请求码
     */
    private final int PHOTO_REQUEST_CAMERA = 0x00aa;
    /**
     * 从相册选择的请求码
     */
    private final int PHOTO_REQUEST_GALLERY = 0x00bb;// 从相册中选择private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    /**
     * 保存拍照文件的临时文件
     */
    private File tempFile;
    /**
     * 拍照文件的文件名称
     */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private GridViewAddImagesAdapter gridViewAddImagesAdapter;

    private Spinner spinner;

    /**
     * 下拉框适配器
     */
    private CommonAdapter<Map<String, Object>> spinnerAdapter;
    /**
     * 数据
     */
    private List<Map<String, Object>> spinnerDataList = new ArrayList<>();


    private Spinner spinnerTeam;

    /**
     * 下拉框适配器
     */
    private CommonAdapter<Map<String, Object>> spinnerTeamAdapter;
    /**
     * 数据
     */
    private List<Map<String, Object>> spinnerTeamList = new ArrayList<>();

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
        spinnerTeam = findViewById(R.id.spinnerTeam);
    }

    @Override
    public void initData() {
        paths.add("http://pic.boxkj.com/testresources%2Fupload%2Fclockphoto75ee101e-2be5-4577-a3a6-41a1f11142bb.jpg?e=1583746126&token=tMj9vn7fPk1I3acqYWULRCosbgeQ3o6T7FW7EbzF:D6i_HwYzrE89RZIHqzdJxZyaHmY=");
        gridViewAddImagesAdapter = new GridViewAddImagesAdapter(paths, mContext, 4, Tools.DPtoPX(10, mContext));
        gridViewAddImagesAdapter.setMaxImages(1);
        gv_images.setAdapter(gridViewAddImagesAdapter);
    }

    @Override
    public void addListener() {
        gv_images.setOnItemClickListener((parent, view, position, id) ->  showDialog());
        bt_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        commit();
    }

    /**
     * 弹出选择对话框
     */
    public void showDialog() {
        View localView = LayoutInflater.from(mContext).inflate(
                R.layout.pub_dialog_add_picture, null);
        TextView tv_camera = (TextView) localView.findViewById(R.id.tv_camera);
        TextView tv_gallery = (TextView) localView.findViewById(R.id.tv_gallery);
        TextView tv_cancel = (TextView) localView.findViewById(R.id.tv_cancel);
        dialog = new Dialog(mContext, R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        // 设置全屏
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        tv_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                /** 拍照**/
                camera();
            }
        });

        tv_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                /** 从系统相册选取照片**/
                gallery();
            }
        });
    }

    /**
     * 拍照
     */
    public void camera() {
        /**判断存储卡是否可以用，可用进行存储**/
        if (hasSdcard()) {
            File dir = new File(Environment.getExternalStorageDirectory() + "/boxin");
            if (!dir.exists()) {
                dir.mkdir();
            }
            tempFile = new File(dir,
                    System.currentTimeMillis() + "_" + PHOTO_FILE_NAME);
            /**从文件中创建uri**/
            Uri uri = Uri.fromFile(tempFile);
            Intent intent = new Intent();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            /**开启一个带有返回值的Activity，PHOTO_REQUEST_CAMERA**/
            startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
        } else {
            Toast.makeText(mContext, "未找到存储卡，无法拍照！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断sdcard是否被挂载
     */
    public boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    /**
     * 从相册获取
     */
    public void gallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 拍照和从相册选择系统回调
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_GALLERY) {
                /** 从相册返回的数据**/
                if (data != null) {
                    /**得到图片的全路径**/
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    /**好像是android多媒体数据库的封装接口，具体的看Android文档**/
                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                    //按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    //最后根据索引值获取图片路径
                    String path = cursor.getString(column_index);
//                    compressImage(path);
                    compressWithLuban(new File(path));
                }

            } else if (requestCode == PHOTO_REQUEST_CAMERA) {
                if (resultCode != RESULT_CANCELED) {
                    /**从相机返回的数据**/
                    if (hasSdcard()) {
                        if (tempFile != null) {
//                            compressImage(tempFile.getPath());
                            compressWithLuban(new File(tempFile.getPath()));
                        } else {
                            Toast.makeText(mContext, "相机异常请稍后再试！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0xAAAAAAAA) {
                photoPath(msg.obj.toString());
            }

        }
    };


    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLuban(final File file) {
        Logger.d("压缩文件路径" + file.getAbsolutePath());
        photoPath(file);
        photoPath(file.getAbsolutePath());
    }

    /**
     * 回调压缩后的图片路径
     */
    public void photoCompressStart(String path) {
    }

    /**
     * 回调压缩后的图片路径
     *
     * @param path 图片路径
     */
    public void photoPath(String path) {
    }

    /**
     * 回调压缩后的图片路径
     *
     * @param file 图片文件
     */
    public void photoPath(final File file) {
        paths.add(file.getPath());
        gridViewAddImagesAdapter.notifyDataSetChanged(paths);

    }

    /**
     * Mapped File  way
     * MappedByteBuffer 可以在处理大文件时，提升性能
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray3(String filename) {

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
//              System.out.println("remain");
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 提交免测申请
     */
    private void commit() {
        String content = et_content.getText().toString().trim();
        if (content.isEmpty()) {
            showToast("申请理由不能为空");
            return;
        }

        if (paths.size() == 0) {
            showToast("至少上传一张图片");
            return;
        }

        StringBuilder fileNames = new StringBuilder();
        StringBuilder imgs = new StringBuilder();
        for (String path : paths) {
            File file = new File(path);
            fileNames.append(fileNames.toString().isEmpty() ? "" : ",");
            fileNames.append(file.getName());
            imgs.append(imgs.toString().isEmpty() ? "" : ",");
            imgs.append(Base64.encode(toByteArray3(file.getPath())));
        }


    }

}
