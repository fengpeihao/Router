package com.cfxc.common.constants

import com.cfxc.router.core.template.IProvider

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/22/21
 */
interface IUserDataProvider:IProvider {
    fun getUserName():String
}