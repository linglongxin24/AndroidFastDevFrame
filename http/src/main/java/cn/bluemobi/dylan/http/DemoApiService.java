package cn.bluemobi.dylan.http;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by yuandl on 2017-03-28.
 */

public interface DemoApiService {

/**********************************************开发实例****************************************************/
    /**
     * 单纯的String参数
     *
     * @param app       Cas
     * @param className Cas_an_UserSign
     * @param phone     phone
     * @param password  password
     * @return
     */
    @FormUrlEncoded
    @POST(" ")
    Observable<ResponseBody> login(@Field("app") String app,
                                   @Field("class") String className,
                                   @Field("phone") String phone,
                                   @Field("password") String password);

    /**
     * 单个文件上传
     * <p>
     * RequestBody requestBodyFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
     * // MultipartBody.Part  和后端约定好Key，这里的partName是用image
     * MultipartBody.Part bodyFile =MultipartBody.Part.createFormData("avarat_img", file.getName(), requestBodyFile);
     *
     * @param app        Cas
     * @param className  Cas_an_UploadAvatar
     * @param sign       sign
     * @param userid     用户Id
     * @param avarat_img 图片
     * @return
     */
    @Multipart
    @POST(" ")
    Observable<ResponseBody> editHeadImage(@Part("app") RequestBody app,
                                           @Part("class") RequestBody className,
                                           @Part("sign") RequestBody sign,
                                           @Part("userid") RequestBody userid,
                                           @Part MultipartBody.Part avarat_img);

    /**
     * 批量文件上传
     * RequestBody task_lon = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(locationBean.getLongitude()));
     * Map<String, RequestBody> photos = new HashMap<>();
     * if (!viewable.onObtainTaskDescribeNetUrl().isEmpty()) {
     * task_img_count = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(viewable.onObtainTaskDescribeNetUrl().size()));
     * for (int i = 0; i < viewable.onObtainTaskDescribeNetUrl().size(); i++) {
     * File file = new File(viewable.onObtainTaskDescribeNetUrl().get(i));
     * RequestBody photo = RequestBody.create(MediaType.parse("image/png"), file);
     * photos.put("task_img" + i + "\"; filename=\"" + file.getName(), photo);
     * }
     * }
     *
     * @param app              Task
     * @param className        Task_an_AddTask
     * @param sign             sign
     * @param userid           用户Id
     * @param task_category_id 任务类型Id
     * @param task_poundage    平台费用
     * @param task_title       任务标题
     * @param task_status_time 任务开始时间
     * @param task_end_time    任务结束时间
     * @param task_content     任务内容详情
     * @param task_special     任务特殊要求
     * @param task_reward      任务酬金
     * @param task_lon         任务 经度
     * @param task_lat         任务 纬度
     * @param task_address     任务地址
     * @param task_img_count   任务图片数量
     * @param task_img         任务图片
     * @return
     */
    @Multipart
    @POST(" ")
    Observable<ResponseBody> publishTask(@Part("app") RequestBody app,
                                         @Part("class") RequestBody className,
                                         @Part("sign") RequestBody sign,
                                         @Part("userid") RequestBody userid,
                                         @Part("task_category_id") RequestBody task_category_id,
                                         @Part("task_poundage") RequestBody task_poundage,
                                         @Part("task_title") RequestBody task_title,
                                         @Part("task_status_time") RequestBody task_status_time,
                                         @Part("task_end_time") RequestBody task_end_time,
                                         @Part("task_content") RequestBody task_content,
                                         @Part("task_special") RequestBody task_special,
                                         @Part("task_reward") RequestBody task_reward,
                                         @Part("task_lon") RequestBody task_lon,
                                         @Part("task_lat") RequestBody task_lat,
                                         @Part("task_address") RequestBody task_address,
                                         @Part("task_img_count") RequestBody task_img_count,
                                         @PartMap Map<String, RequestBody> task_img);
}
