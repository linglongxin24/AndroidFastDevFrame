package com.dylanfastdev

/**
 * @author YDL
 * @date 2021/01/04/14:54
 * @version 1.0
 */
class MyListen() {
    private var callBack: CallBack? = null
    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    interface CallBack {
        fun open(data: Map<String, Any>)
    }
}