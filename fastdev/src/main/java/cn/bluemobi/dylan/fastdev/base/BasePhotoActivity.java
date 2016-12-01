package cn.bluemobi.dylan.fastdev.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;

import cn.bluemobi.dylan.fastdev.R;
import cn.bluemobi.dylan.fastdev.autolayout.utils.AutoUtils;
import cn.bluemobi.dylan.fastdev.config.FilePath;
import cn.bluemobi.dylan.fastdev.utils.NativeUtil;

/**
 * Created by kangfh on 2016/6/1.
 */
public abstract class BasePhotoActivity extends BaseActivity  {
    private Dialog dialog;
    private  final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private  final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";



    public void showdialog() {
        View localView = LayoutInflater.from(context).inflate(
                R.layout.pub_dialog_add_picture, null);
        AutoUtils.auto(localView.getRootView());
        TextView tv_camera = (TextView) localView.findViewById(R.id.tv_camera);
        TextView tv_gallery = (TextView) localView.findViewById(R.id.tv_gallery);
        TextView tv_cancel = (TextView) localView.findViewById(R.id.tv_cancel);
        dialog = new Dialog(context, R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        // 设置全屏
        WindowManager windowManager = ((Activity) context).getWindowManager();
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
                // 拍照
//                cameraSamsung();
                camera();
            }
        });

        tv_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 从系统相册选取照片
                gallery();
            }
        });
    }

    public void camera() {
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
//            File file = new File(FilePath.IMAGE_DIR + System.currentTimeMillis() + ".jpg");
//            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
//            Uri imageUri = Uri.fromFile(file);
//            getTakePhoto().onEnableCompress(new CompressConfig.Builder().setMaxSize(100 * 1024).setMaxPixel(1280).create(), true).onPicTakeOriginal(imageUri);
/**
 *
 */
            File dir = new File(FilePath.IMAGE_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }
            tempFile = new File(dir,
                    System.currentTimeMillis() + "_" + PHOTO_FILE_NAME);
            //从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            Intent intent = new Intent();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(intent.CATEGORY_DEFAULT);
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
            startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
        }else{
            Toast.makeText(context, "未找到存储卡，无法拍照！", Toast.LENGTH_SHORT).show();
        }
//        Intent intent2 = CameraProtectActivity.createIntent(this);
//     startActivityForResult(intent2, PHOTO_REQUEST_CAREMA);
//        Intent intent = new Intent(context,
//                UseCameraActivity.class);
//        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    /*
    * 判断sdcard是否被挂载
     */
    public boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    /*
   * 从相册获取2
   */
    public void gallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_GALLERY) {
                // 从相册返回的数据
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    //好像是android多媒体数据库的封装接口，具体的看Android文档
                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                    //按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    //最后根据索引值获取图片路径
                    String path = cursor.getString(column_index);
//                Logger.d(" uri.getPath()=" + path);


                    uploadImage(path);
                }

            } else if (requestCode == PHOTO_REQUEST_CAREMA) {
                if (resultCode != RESULT_CANCELED) {
                    // 从相机返回的数据
                    if (hasSdcard()) {
//                        String path = data.getStringExtra(UseCameraActivity.IMAGE_PATH);
//                        uploadImage(path);
//                        String path = data.getStringExtra(CameraProtectActivity.EXTRA_IMG_PATH);
//                        uploadImage(path);

                        if (tempFile != null) {
                            uploadImage(tempFile.getPath());
                        } else {
                            Toast.makeText(context, "相机异常请稍后再试！", Toast.LENGTH_SHORT).show();
                        }

                        Logger.i("拿到照片path=" + tempFile.getPath());
                    } else {
                        Toast.makeText(context, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
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
     * 上传图片
     *
     * @param path
     */
    private void uploadImage(final String path) {
        new Thread() {
            @Override
            public void run() {
                if(new File(path).exists()){
                    Logger.d("源文件存在"+path);
                }else{
                    Logger.d("源文件不存在"+path);
                }

                File dir = new File(FilePath.IMAGE_DIR);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                final File file = new File(dir + "/temp_photo" + System.currentTimeMillis() + ".jpg");
//                /**
//                 * 获取bitmap
//                 */
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                /**
//                 * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
//                 */
//                int degree = readPictureDegree(path);
//                bitmap = rotaingBitmap(bitmap, degree);
//                /**
//                 * 压缩图片
//                 */
//                NativeUtil.compressBitmap(bitmap, 50,
//                        file.getAbsolutePath(), true);
//                /**
//                 * 及时回收bitmap防止内存溢出
//                 */
//                if (bitmap != null) {
//                    bitmap.recycle();
//                    System.gc();
//                }
                NativeUtil.compressBitmap(path, file.getAbsolutePath(), 50);
                if(file.exists()){
                    Logger.d("压缩后的文件存在"+file.getAbsolutePath());
                }else{
                    Logger.d("压缩后的不存在"+file.getAbsolutePath());
                }
                /**
                 * 删除源文件
                 */
//                File oldFile = new File(path);
//                if (oldFile.getParent().contains("jingzhi")) {
//                    oldFile.delete();
//                }
                Message message = new Message();
                message.what = 0xAAAAAAAA;
                message.obj = file.getAbsolutePath();
                handler.sendMessage(message);

            }
        }.start();

    }

    public void photoPath(String path) {
        if(new File(path).exists()){
            Logger.d("父类photoPath获取的文件存在"+path);
        }else{
            Logger.d("父类photoPath获取的文件不存在"+path);
        }
    }
}
