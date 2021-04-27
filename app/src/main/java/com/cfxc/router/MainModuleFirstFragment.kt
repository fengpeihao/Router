package com.cfxc.router

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cfxc.common.constants.RouteConstant
import com.cfxc.router.annotation.Route
import kotlinx.android.synthetic.main.fragment_main_module_first.*

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/6/21
 */
@Route(destinationText = RouteConstant.MAIN_MODULE_FIRST_FRAGMENT)
class MainModuleFirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_module_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            tv_received.text = it.toString()
        }
    }
}