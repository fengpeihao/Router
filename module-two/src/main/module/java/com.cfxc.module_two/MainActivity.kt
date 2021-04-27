package com.cfxc.module_two

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cfxc.router.core.template.Router
import com.example.module_two.R

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/26/21
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Router.init(application)
    }

}