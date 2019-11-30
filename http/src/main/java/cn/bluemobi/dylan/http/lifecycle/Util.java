package cn.bluemobi.dylan.http.lifecycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zr on 2018/7/23.
 */

public class Util {
    public static <T> List<T> getSnapshot(Collection<T> other) {
        // toArray creates a new ArrayList internally and this way we can guarantee entries will not
        // be null. See #322.
        List<T> result = new ArrayList<T>(other.size());
        for (T item : other) {
            result.add(item);
        }
        return result;
    }
}
