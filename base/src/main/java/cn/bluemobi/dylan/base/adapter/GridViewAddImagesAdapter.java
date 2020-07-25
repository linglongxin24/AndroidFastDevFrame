package cn.bluemobi.dylan.base.adapter;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bluemobi.dylan.base.R;
import cn.bluemobi.dylan.base.utils.activitypermission.ActPermissionRequest;
import cn.bluemobi.dylan.base.utils.activityresult.ActResultRequest;
import cn.bluemobi.dylan.base.view.iOSOneButtonDialog;
import cn.bluemobi.dylan.photoview.PhotoScaleUtils;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_CANCELED;


/**
 * @author yuandl on 2015/12/24.
 * 添加上传图片适配器
 */
public class GridViewAddImagesAdapter extends BaseAdapter {
    private List<String> paths = new ArrayList<>();
    private Context mContext;
    private FragmentActivity mActivity;
    private LayoutInflater inflater;
    private int itemWidth;

    private int addImageResourceId = R.drawable.image_add;
    /**
     * 可以动态设置最多上传几张，之后就不显示+号了，用户也无法上传了
     * 默认9张
     */
    private int maxImages = 9;

    public GridViewAddImagesAdapter(FragmentActivity mActivity, int numColumns, int spacing) {
        this.mActivity = mActivity;
        this.mContext = mActivity;
        int displayWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        itemWidth = (displayWidth - spacing * (numColumns + 1)) / numColumns;
        inflater = LayoutInflater.from(mContext);
    }

    /**
     * 设置默认的加号图片
     *
     * @param addImageResourceId
     * @return
     */
    public GridViewAddImagesAdapter setAddImageResourceId(int addImageResourceId) {
        this.addImageResourceId = addImageResourceId;
        return this;
    }

    /**
     * 获取最大上传张数
     *
     * @return
     */
    public int getMaxImages() {
        return maxImages;
    }

    /**
     * 设置最大上传张数
     *
     * @param maxImages
     */
    public GridViewAddImagesAdapter setMaxImages(int maxImages) {
        this.maxImages = maxImages;
        return this;
    }

    /**
     * 让GridView中的数据数目加1最后一个显示+号
     * 当到达最大张数时不再显示+号
     *
     * @return 返回GridView中的数量
     */
    @Override
    public int getCount() {
        int count = paths == null ? 1 : paths.size() + 1;
        if (count > maxImages) {
            return paths.size();
        } else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged(List<String> paths) {
        this.paths = paths;
        this.notifyDataSetChanged();

    }

    public List<String> getPaths() {
        return paths;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pub_item_grid_add_image, parent, false);
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, itemWidth));
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**代表+号之前的需要正常显示图片**/
        if (paths != null && position < paths.size()) {
            final File file = new File(paths.get(position));
            Glide.with(mContext)
                    .load(paths.get(position).startsWith("http") ? paths.get(position) : file)
                    .priority(Priority.HIGH)
                    .centerCrop()
                    .into(viewHolder.ivimage);
            viewHolder.btdel.setVisibility(View.VISIBLE);
            viewHolder.btdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (file.exists()) {
                        file.delete();
                    }
                    paths.remove(position);
                    notifyDataSetChanged();
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoScaleUtils.scale(mContext, paths, position);
                }
            });
        } else {
            /**代表+号的需要+号图片显示图片**/
            Glide.with(mContext)
                    .load(addImageResourceId)
                    .priority(Priority.HIGH)
                    .into(viewHolder.ivimage);
            viewHolder.ivimage.setScaleType(ImageView.ScaleType.FIT_XY);
            viewHolder.btdel.setVisibility(View.GONE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ActPermissionRequest(mActivity).requestPermission(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .setPermissionCheckCallBack(new ActPermissionRequest.PermissionCheckCallBack() {
                                @Override
                                public void onSucceed() {
                                    showDialog();
                                }

                                @Override
                                public void onReject(String... permission) {
                                    new iOSOneButtonDialog(mContext).setMessage("您拒绝了拍照和存储权限，无法拍照上传").show();
                                }

                                @Override
                                public void onRejectAndNoAsk(String... permission) {
                                    new iOSOneButtonDialog(mContext).setMessage("您拒绝了拍照和存储权限，无法拍照上传").show();

                                }
                            });

                }
            });
        }

        return convertView;

    }

    public class ViewHolder {
        public final ImageView ivimage;
        public final Button btdel;
        public final View root;

        public ViewHolder(View root) {
            ivimage = (ImageView) root.findViewById(R.id.iv_image);
            btdel = (Button) root.findViewById(R.id.bt_del);
            this.root = root;
        }
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
        final Dialog dialog = new Dialog(mContext, R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        // 设置全屏

        Display display = mActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置宽度
        lp.width = width;
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
     * 保存拍照文件的临时文件
     */
    private File tempFile;
    /**
     * 拍照文件的文件名称
     */
    private final String PHOTO_FILE_NAME = "temp_photo.jpg";

    /**
     * 拍照
     */
    public void camera() {
        /**判断存储卡是否可以用，可用进行存储**/
        if (hasSdcard()) {
            File dir = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                dir = mContext.getExternalCacheDir();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
            if (dir == null || !dir.exists()) {
                dir = mContext.getCacheDir();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
            File fileFolder = new File(dir, "temp");
            if (!fileFolder.exists()) {
                fileFolder.mkdirs();
            }
            tempFile = new File(fileFolder, System.currentTimeMillis() + "_" + PHOTO_FILE_NAME);
            Uri uri;
            /**从文件中创建uri**/
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                uri = Uri.fromFile(tempFile);
            } else {
                uri = FileProvider.getUriForFile(mContext, mContext.getApplicationInfo().packageName + ".provider", tempFile);
            }
            Intent intent = new Intent();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            /**开启一个带有返回值的Activity，PHOTO_REQUEST_CAMERA**/
            new ActResultRequest(mActivity).startForResult(intent, new ActResultRequest.Callback() {
                @Override
                public void onActivityResult(int resultCode, Intent data) {
                    if (resultCode != RESULT_CANCELED) {
                        /**从相机返回的数据**/
                        if (hasSdcard()) {
                            if (tempFile != null) {
                                compressWithLuban(new File(tempFile.getPath()));
                            } else {
                                Toast.makeText(mContext, "相机异常请稍后再试！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
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
        new ActResultRequest(mActivity).startForResult(intent, new ActResultRequest.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                /** 从相册返回的数据**/
                if (data != null) {
                    /**得到图片的全路径**/
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    /**好像是android多媒体数据库的封装接口，具体的看Android文档**/
                    Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                    //按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    //最后根据索引值获取图片路径
                    String path = cursor.getString(column_index);
                    compressWithLuban(new File(path));
                }
            }
        });

    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLuban(final File file) {
        Luban.with(mContext)
                .load(file)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        paths.add(file.getPath());
                        notifyDataSetChanged(paths);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }
}
