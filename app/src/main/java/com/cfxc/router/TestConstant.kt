package com.cfxc.router

import com.cfxc.common.constants.RouteConstant

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/21/21
 */
object TestConstant {

    var isNeedLogin = true

    var isNeedPrerequisite = true

    fun checkNeedPrerequisite(destination: String): Boolean {
        return (RouteConstant.MAIN_MODULE_SECOND_FRAGMENT == destination || RouteConstant.MAIN_MODULE_THIRD_FRAGMENT == destination) && isNeedPrerequisite
    }

    fun checkNeedLogin(destination: String): Boolean {
        return (RouteConstant.MAIN_MODULE_FIRST_FRAGMENT == destination || RouteConstant.MAIN_MODULE_THIRD_FRAGMENT == destination) && isNeedLogin
    }
}