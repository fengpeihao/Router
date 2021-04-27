package com.cfxc.router

import com.cfxc.common.constants.IUserDataProvider
import com.cfxc.common.constants.RouteConstant
import com.cfxc.router.annotation.Route
import com.cfxc.router.core.template.IProvider

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/22/21
 */
@Route(destinationText = RouteConstant.USER_DATA_PROVIDER)
class UserDataProvider: IUserDataProvider {

    override fun getUserName():String{
        return "Tom"
    }
}