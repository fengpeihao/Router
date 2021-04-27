package com.cfxc.module_two

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.cfxc.router.annotation.Constants
import com.example.module_two.R

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/9/21
 */
class ModuleTwoNavigatorFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val destinationId = it.getInt(Constants.KEY_DESTINATION_ID, R.id.moduleTwoFirstFragment)
            findNavController().navigate(
                destinationId, it,
                NavOptions.Builder().setPopUpTo(R.id.moduleTwoNavigatorFragment, true)
                    .setEnterAnim(R.anim.slide_in).setExitAnim(R.anim.slide_out).build()
            )
        } ?: run {
            findNavController().navigate(
                R.id.moduleTwoFirstFragment, null,
                NavOptions.Builder().setPopUpTo(R.id.moduleTwoNavigatorFragment, true)
                    .setEnterAnim(R.anim.slide_in).setExitAnim(R.anim.slide_out).build()
            )
        }
    }
}