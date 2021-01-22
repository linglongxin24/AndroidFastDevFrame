package com.dylanfastdev

import cn.bluemobi.dylan.http.HttpJsonKey

/**
 * @author YDL
 * @date 2021/01/22/11:07
 * @version 1.0
 */
class HttpJsonKeyImp : HttpJsonKey {
    override fun getCode(): String {
        return "status"
    }

    override fun getData(): String {
        return "data"
    }

    override fun getMsg(): String {
        return "message"
    }

    override fun getSuccessCode(): Int {
        return 200
    }
}