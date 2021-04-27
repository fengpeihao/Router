package com.cfxc.router.dispatcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cfxc.router.MainActivity


/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/14/21
 */
class DispatcherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity::class.java)
        intent.data = getIntent()?.data
        startActivity(intent)
        finish()
    }
}