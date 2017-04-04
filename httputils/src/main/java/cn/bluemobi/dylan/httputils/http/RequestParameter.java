package cn.bluemobi.dylan.httputils.http;

import android.support.v4.util.ArrayMap;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 请求参数工具类
 * Created by dylan on 2017-04-04.
 */

public class RequestParameter {
    /**
     * 获取String通过Part封装后的RequestBody
     *
     * @param value
     * @return
     */
    public static RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    /**
     * 获取File通过Part封装后的RequestBody
     *
     * @param file
     * @return
     */
    public static RequestBody getRequestBody(File file) {
        return RequestBody.create(MediaType.parse("image/png"), file);
    }

    /**
     * 获取单个文件参数
     *
     * @param key  parameter_key
     * @param file 要上传的文件
     * @return
     */
    public static MultipartBody.Part getFilePart(String key, File file) {
        return MultipartBody.Part.createFormData(key, file.getName(), getRequestBody(file));
    }

    /**
     * 获取文件数组组装后的参数
     *
     * @param key   parameter_key
     * @param files 要上传的文件集合
     * @return
     */
    public static Map<String, RequestBody> getFilePartMap(String key, List<File> files) {
        Map<String, RequestBody> paramsMap = new ArrayMap<>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
            paramsMap.put(key + "\"; filename=\"" + file.getName(), fileBody);
        }
        return paramsMap;
    }
}
