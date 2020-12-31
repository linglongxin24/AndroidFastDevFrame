package cn.bluemobi.dylan.smartwebview;

/**
 * webview 加载结果返回接口
 */
interface WebLoadCallBack {
    fun onLoaded(url: String);
}