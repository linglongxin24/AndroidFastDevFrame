package cn.bluemobi.dylan.base.utils.activityresult;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.SparseArray;

/**
 * @author Kale
 * @date 2018/4/13
 */
public class OnActResultEventDispatcherFragment extends Fragment {

    public static final String TAG = "on_act_result_event_dispatcher";

    private SparseArray<ActResultRequest.Callback> mCallbacks = new SparseArray<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startForResult(Intent intent, ActResultRequest.Callback callback) {
        int key = randomRequestCode();
        mCallbacks.put(key, callback);
        startActivityForResult(intent, key);
    }

    private int randomRequestCode() {
        int number = (int) (Math.random() * 100) + 1;
        while (mCallbacks.indexOfKey(number) != -1) {
            number = (int) (Math.random() * 100) + 1;
        }
        return number;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ActResultRequest.Callback callback = mCallbacks.get(requestCode);
        mCallbacks.remove(requestCode);

        if (callback != null) {
            callback.onActivityResult(resultCode, data);
        }
    }
}