package com.cfxc.router

import android.app.Application
import com.cfxc.router.core.template.Router

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/7/21
 */
class App : Application() {

    companion object {
        lateinit var application: App
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        Router.init(this)
    }
}