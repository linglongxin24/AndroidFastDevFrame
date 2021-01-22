package cn.bluemobi.dylan.http

/**
 * @author YDL
 * @date 2021/01/22/11:06
 * @version 1.0
 */
interface HttpJsonKey {
    fun getCode():String
    fun getData():String
    fun getMsg():String
    fun getSuccessCode():Int
}