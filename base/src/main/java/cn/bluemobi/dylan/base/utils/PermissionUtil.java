package cn.bluemobi.dylan.base.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限申请工具类
 */
public class PermissionUtil {

    /**请求权限码**/
    public static final int REQUEST_PERMISSION=111;
    /**请求打开系统设置码**/
    public static final int REQUEST_OPEN_SETTING=222;


    /**
     * 检测权限
     *
     * @return true：已授权； false：未授权；
     */
    public static boolean checkPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    /**
     * 检测多个权限
     *
     * @return 未授权的权限
     */
    public static List<String> checkMorePermissions(Context context, String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (!checkPermission(context, permissions[i]))
                permissionList.add(permissions[i]);
        }
        return permissionList;
    }

    /**
     * 请求权限
     */
    public static void requestPermission(Context context, String permission, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
    }

    /**
     * 请求多个权限
     */
    public static void requestMorePermissions(Context context, List permissionList, int requestCode) {
        String[] permissions = (String[]) permissionList.toArray(new String[permissionList.size()]);
        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
    }


    /**
     * 判断是否已拒绝过权限
     * 如果应用之前请求过此权限但用户拒绝，此方法将返回 true;
     * 如果应用第一次请求权限或 用户在过去拒绝了权限请求，
     * 并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
     */
    public static boolean judgePermission(Context context, String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission))
            return true;
        else
            return false;
    }

    /**
     * 检测权限并请求权限：如果没有权限，则请求权限
     */
    public static void checkAndRequestPermission(Context context, String permission, int requestCode) {
        if (!checkPermission(context, permission)) {
            requestPermission(context, permission, requestCode);
        }
    }

    /**
     * 检测并请求多个权限
     */
    public static void checkAndRequestMorePermissions(Context context, String[] permissions, int requestCode) {
        List<String> permissionList = checkMorePermissions(context, permissions);
        if(permissionList.size()>0){
            requestMorePermissions(context, permissionList, requestCode);
        }
    }


    /**
     * 检测权限
     * 具体实现由回调接口决定
     */
    public static void checkPermission(Context context, String permission, PermissionCheckCallBack callBack) {
        if (checkPermission(context, permission)) { // 用户已授予权限
            callBack.onSucceed();
        } else {
            if (judgePermission(context, permission))  // 用户之前已拒绝过权限申请
                callBack.onReject(permission);
            else                                       // 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
                callBack.onRejectAndNoAsk(permission);
        }
    }

    /**
     * 检测多个权限，并回调
     * 具体实现由回调接口决定
     */
    public static void checkMorePermissions(Context context, String[] permissions, PermissionCheckCallBack callBack) {
        List<String> permissionList = checkMorePermissions(context, permissions);
        if (permissionList.size() == 0) {  // 用户已授予权限
            callBack.onSucceed();
        } else {
            boolean isFirst = true;
            for (int i = 0; i < permissionList.size(); i++) {
                String permission = permissionList.get(i);
                if (judgePermission(context, permission)) {
                    isFirst = false;
                    break;
                }
            }
            String[] unauthorizedMorePermissions = (String[]) permissionList.toArray(new String[permissionList.size()]);
            if (isFirst)// 用户之前已拒绝过权限申请
                callBack.onReject(unauthorizedMorePermissions);
            else       // 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
                callBack.onRejectAndNoAsk(unauthorizedMorePermissions);

        }
    }



    /**
     * 判断权限是否申请成功
     */
    public static boolean isPermissionRequestSuccess(int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    /**
     * 用户申请权限返回，回调
     */
    public static void onRequestPermissionResult(Context context, String permission, int[] grantResults, PermissionCheckCallBack callback) {
        if (PermissionUtil.isPermissionRequestSuccess(grantResults)) {
            callback.onSucceed();
        } else {
            if (PermissionUtil.judgePermission(context, permission)) {
                callback.onReject(permission);
            } else {
                callback.onRejectAndNoAsk(permission);
            }
        }
    }

    /**
     * 用户申请多个权限返回，回调
     */
    public static void onRequestMorePermissionsResult(Context context, String[] permissions, PermissionCheckCallBack callback) {
        boolean isBannedPermission = false;
        List<String> permissionList = checkMorePermissions(context, permissions);
        if (permissionList.size() == 0)
            callback.onSucceed();
        else {
            for (int i = 0; i < permissionList.size(); i++) {
                if (!judgePermission(context, permissionList.get(i))) {
                    isBannedPermission = true;
                    break;
                }
            }
            //　已禁止再次询问权限
            if (isBannedPermission)
                callback.onRejectAndNoAsk(permissions);
            else // 拒绝权限
                callback.onReject(permissions);
        }

    }

    public interface PermissionCheckCallBack {

        /**
         * 用户已授予权限
         */
        void onSucceed();

        /**
         * 用户已拒绝过权限
         * @param permission:被拒绝的权限
         */
        void onReject(String... permission);

        /**
         * 用户已拒绝过并且已勾选不再询问选项、用户第一次申请权限;
         * @param permission:被拒绝的权限
         */
        void onRejectAndNoAsk(String... permission);
    }

    /**
     * 判断名称并，显示对应的提示对话框
     * @param mContext 上下文对象
     * @param array 权限集合
     * @param isFinish 是否关闭界面，true————>关闭界面，false————>关闭对话框，自行处理逻辑
     */
    public static void judgeNameShowToSettingDialog(Activity mContext, String[] array, boolean isFinish){
        for (int i = 0; i < array.length; i++) {
            if(array[i].equals("android.permission.WRITE_EXTERNAL_STORAGE")){
                showToSettingDialog(mContext,"存储",isFinish);
                break;
            }
            if(array[i].equals("android.permission.CAMERA")){
                showToSettingDialog(mContext,"相机",isFinish);
                break;
            }
            if(array[i].equals("android.permission.READ_CONTACTS")){
                showToSettingDialog(mContext,"通讯录",isFinish);
                break;
            }
            if(array[i].equals("android.permission.RECORD_AUDIO")){
                showToSettingDialog(mContext,"麦克风",isFinish);
                break;
            }
        }
    }


    /**
     * 当用户选择了拒绝并不再提示，显示自定义提示对话框
     * @param mContext 上下文对象
     * @param permissionName 权限中文名称
     */
    public static void showToSettingDialog(final Activity mContext, String permissionName, final boolean isFinish){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext)
                .setTitle(permissionName+"权限不可用")
                .setMessage("请在设置中打开"+permissionName+"权限")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        toAppSetting(mContext);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(isFinish){
                            mContext.finish();
                        }
                    }
                }).setCancelable(false);
        dialog.show();
    }


    /**
     * 跳转到权限设置界面
     */
    public static void toAppSetting(Activity context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivityForResult(intent,REQUEST_OPEN_SETTING);
    }



}
