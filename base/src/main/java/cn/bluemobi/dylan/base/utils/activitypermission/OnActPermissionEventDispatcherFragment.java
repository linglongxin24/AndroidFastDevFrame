package cn.bluemobi.dylan.base.utils.activitypermission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kale
 * @date 2018/4/13
 */
public class OnActPermissionEventDispatcherFragment extends Fragment {

    public static final String TAG = "on_act_permission_event_dispatcher";

    private SparseArray<ActPermissionRequest.PermissionCheckCallBack> mCallbacks = new SparseArray<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void requestPermissions(String[] permissions, ActPermissionRequest.PermissionCheckCallBack callback) {
        List<String> permissionList = checkMorePermissions(getContext(), permissions);
        if (permissionList.size() > 0) {
            int key = randomRequestCode();
            mCallbacks.put(key, callback);
            requestPermissions(permissions, key);
        } else {
            if (callback != null) {
                callback.onSucceed();
            }
        }

    }

    private int randomRequestCode() {
        int number = (int) (Math.random() * 100) + 1;
        while (mCallbacks.indexOfKey(number) != -1) {
            number = (int) (Math.random() * 100) + 1;
        }
        return number;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActPermissionRequest.PermissionCheckCallBack callback = mCallbacks.get(requestCode);
        mCallbacks.remove(requestCode);
        if (callback == null) {
            return;
        }
        List<String> permissionList = checkMorePermissions(getContext(), permissions);
        if (permissionList.size() == 0) {
            callback.onSucceed();
        } else {
            boolean isBannedPermission = false;
            for (int i = 0; i < permissionList.size(); i++) {
                if (!judgePermission(getContext(), permissionList.get(i))) {
                    isBannedPermission = true;
                    break;
                }
            }
            if (isBannedPermission) {
                //　已禁止再次询问权限
                callback.onRejectAndNoAsk(permissions);
            } else {
                // 拒绝权限
                callback.onReject(permissions);
            }
        }
    }

    /**
     * 检测权限
     *
     * @return true：已授权； false：未授权；
     */
    public boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检测多个权限
     *
     * @return 未授权的权限
     */
    public List<String> checkMorePermissions(Context context, String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (!checkPermission(context, permissions[i])) {
                permissionList.add(permissions[i]);
            }
        }
        return permissionList;
    }

    /**
     * 判断是否已拒绝过权限
     * 如果应用之前请求过此权限但用户拒绝，此方法将返回 true;
     * 如果应用第一次请求权限或 用户在过去拒绝了权限请求，
     * 并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
     */
    public boolean judgePermission(Context context, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
    }

}