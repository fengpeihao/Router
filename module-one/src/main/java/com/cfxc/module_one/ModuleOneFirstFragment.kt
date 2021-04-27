package com.cfxc.module_one

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cfxc.common.constants.BundleKeyConstant
import com.cfxc.common.constants.IUserDataProvider
import com.cfxc.common.constants.RequestKeyConstant
import com.cfxc.common.constants.RouteConstant
import com.example.module_one.R
import com.cfxc.router.annotation.Route
import com.cfxc.router.core.template.Router
import kotlinx.android.synthetic.main.fragment_module_one_first.*

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/8/21
 */
@Route(destinationText = RouteConstant.MODULE_ONE_FIRST_FRAGMENT)
class ModuleOneFirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_module_one_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_goto_next.setOnClickListener {
            Router.getInstance().build(RouteConstant.MODULE_TWO_FIRST_FRAGMENT)
                .with(bundleOf(BundleKeyConstant.KEY_CONTENT to "module one says 'Hello'"))
                .navigation(findNavController())
        }

        parentFragmentManager.setFragmentResultListener(
            RequestKeyConstant.KEY_FIRST_CONTACT_SECOND,
            viewLifecycleOwner,
            { requestKey, result ->
                if (requestKey == RequestKeyConstant.KEY_FIRST_CONTACT_SECOND) {
                    tv_received.text = result.getString(BundleKeyConstant.KEY_CONTENT)
                }
            })

        btn_get_app_module_data.setOnClickListener {
            val userDataProvider = Router.getInstance().build(RouteConstant.USER_DATA_PROVIDER)
                .navigation() as IUserDataProvider
            Toast.makeText(requireContext(), userDataProvider.getUserName(), Toast.LENGTH_SHORT)
                .show()
        }

        btn_goto_second.setOnClickListener {
            Router.getInstance().build(RouteConstant.MODULE_ONE_SECOND_FRAGMENT)
                .navigation(findNavController())
        }
    }
}