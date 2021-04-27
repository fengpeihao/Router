package com.cfxc.router

import android.content.Context
import android.util.Log
import com.cfxc.common.constants.RouteConstant
import com.cfxc.router.TestConstant.checkNeedLogin
import com.cfxc.router.annotation.Interceptor
import com.cfxc.router.core.Postcard
import com.cfxc.router.core.callback.InterceptorCallback
import com.cfxc.router.core.template.IInterceptor
import com.cfxc.router.core.template.Router

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/21/21
 */
@Interceptor(priority = 2, name = "loginInterceptor")
class LoginInterceptor : IInterceptor {
    val TAG = "LoginInterceptor"

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        if (checkNeedLogin(postcard.destinationText)) {
            postcard.prerequisiteDestinationGraph = "nav_graph"
            postcard.prerequisiteDestination = RouteConstant.LOGIN_FRAGMENT
        }
        callback.onContinue(postcard)
    }

    override fun init(context: Context?) {
        Log.e(TAG, "LoginInterceptor init")
    }
}