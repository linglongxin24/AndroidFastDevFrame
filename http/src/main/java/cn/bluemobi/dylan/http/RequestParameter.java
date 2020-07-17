package cn.bluemobi.dylan.http;

import androidx.collection.ArrayMap;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 请求参数工具类
 *
 * @author dylan
 * @date 2017-04-04
 */

public class RequestParameter {
    /**
     * 获取String通过Part封装后的RequestBody
     *
     * @param value 值 @Part("app") RequestBody app
     * @return RequestBody
     */
    public static RequestBody getStringRequestBody(String value) {
        return RequestBody.Companion.create(value, MediaType.parse("multipart/form-data"));
    }

    /**
     * 获取图片File通过Part封装后的RequestBody
     *
     * @param file 图片文件 @Part("app") RequestBody app
     * @return RequestBody
     */
    public static RequestBody getImageRequestBody(File file) {
        return getFileRequestBody(file, MediaType.parse("image/png"));
    }

    /**
     * 获取File通过Part封装后的RequestBody @Part("app") RequestBody app
     *
     * @param file      文件
     * @param mediaType 文件类型
     * @return RequestBody
     */
    public static RequestBody getFileRequestBody(File file, MediaType mediaType) {
        return RequestBody.Companion.create(file, mediaType);
    }


    /**
     * 获取单个图片文件参数 @Part MultipartBody.Part
     *
     * @param key  parameter_key
     * @param file 要上传的文件
     * @return RequestBody
     */
    public static MultipartBody.Part getImageFilePart(String key, File file) {
        return MultipartBody.Part.createFormData(key, file.getName(), getImageRequestBody(file));
    }

    /**
     * 获取单个文件参数
     *
     * @param key       文件key
     * @param file      文件
     * @param mediaType 文件类型
     * @return RequestBody
     */
    public static MultipartBody.Part getFilePart(String key, File file, MediaType mediaType) {
        return MultipartBody.Part.createFormData(key, file.getName(), getFileRequestBody(file, mediaType));
    }

    /**
     * 获取文件数组组装后的参数 @PartMap Map<String, RequestBody>
     *
     * @param key   parameter_key
     * @param files 要上传的文件集合
     * @return RequestBody
     */
    public static Map<String, RequestBody> getFilePartMap(String key, List<File> files, MediaType mediaType) {
        Map<String, RequestBody> paramsMap = new ArrayMap<>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            RequestBody fileBody = getFileRequestBody(file, mediaType);
            paramsMap.put(key + "\"; filename=\"" + file.getName(), fileBody);
        }
        return paramsMap;
    }

    /**
     * 获取图片文件数组组装后的参数 @PartMap Map<String, RequestBody>
     *
     * @param key   parameter_key
     * @param files 要上传的文件集合
     * @return RequestBody
     */
    public static Map<String, RequestBody> getImageFilePartMap(String key, List<File> files) {
        return getFilePartMap(key, files, MediaType.parse("image/png"));
    }
}
