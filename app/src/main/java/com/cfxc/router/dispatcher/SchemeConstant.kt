package com.cfxc.router.dispatcher

import com.cfxc.router.App
import com.cfxc.router.R

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/6/21
 */
object SchemeConstant {
    val SCHEME = App.application.getString(R.string.scheme)
    val HOST = App.application.getString(R.string.host)

    val KEY_SCHEME_CONTENT = "scheme_content"
}