package cn.bluemobi.dylan.http.download

/**
 * Created by YDL on 2017/6/8./ **
 * 响应体进度回调接口，用于文件下载进度回调
 */
interface ProgressResponseListener {
    fun onResponseProgress(bytesRead: Long, contentLength: Long, done: Boolean)
}