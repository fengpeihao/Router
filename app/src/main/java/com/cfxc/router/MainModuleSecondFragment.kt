package com.cfxc.router

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.cfxc.common.constants.RouteConstant
import com.cfxc.router.annotation.Route
import kotlinx.android.synthetic.main.fragment_main_module_second.*

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/22/21
 */
@Route(destinationText = RouteConstant.MAIN_MODULE_SECOND_FRAGMENT)
class MainModuleSecondFragment: Fragment(R.layout.fragment_main_module_second) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            tv_received.text = it.toString()
        }
    }
}