package com.cfxc.module_two

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cfxc.common.constants.BundleKeyConstant
import com.cfxc.common.constants.RequestKeyConstant
import com.cfxc.common.constants.RouteConstant
import com.cfxc.router.annotation.Route
import com.cfxc.router.core.template.Router
import com.example.module_two.R
import kotlinx.android.synthetic.main.fragment_module_two_first.*

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/8/21
 */
@Route(destinationText = RouteConstant.MODULE_TWO_FIRST_FRAGMENT)
class ModuleTwoFirstFragment : Fragment(R.layout.fragment_module_two_first) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_received.text = arguments?.getString(BundleKeyConstant.KEY_CONTENT) ?: "null"

        btn_return_back.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                RequestKeyConstant.KEY_FIRST_CONTACT_SECOND,
                bundleOf(BundleKeyConstant.KEY_CONTENT to "module two says ${edt_return_content.text}")
            )
            findNavController().popBackStack()
        }

        btn_goto_main_home.setOnClickListener {
            Router.getInstance().build(RouteConstant.MAIN_MODULE_HOME_FRAGMENT)
                .navigation(findNavController())
        }
    }
}