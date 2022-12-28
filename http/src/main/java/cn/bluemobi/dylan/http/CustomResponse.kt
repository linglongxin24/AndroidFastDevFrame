package cn.bluemobi.dylan.http

import android.content.Context
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * @author Dylan
 * @date 2022/11/14/17:54
 * @version 1.0
 */
interface CustomResponse {
    fun onNext(context: Context,httpRequest: HttpRequest,httpResponse: HttpResponse,responseBodyResponse: Response<ResponseBody?>?)
    fun onError(context: Context,httpRequest: HttpRequest,httpResponse: HttpResponse,e: Throwable?)
}