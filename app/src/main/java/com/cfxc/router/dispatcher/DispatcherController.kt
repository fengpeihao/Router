package com.cfxc.router.dispatcher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.navigation.NavController
import com.cfxc.router.core.template.Router
import com.cfxc.router.dispatcher.SchemeConstant.KEY_SCHEME_CONTENT

/**
 * @description
 * @author: created by peihao.feng
 * @date: 4/6/21
 */
object DispatcherController {

    fun dispatch(navController: NavController, intent: Intent) {
        val uri = intent.data ?: return
        //allUrl-> scheme://host/path?scheme_content=intent:#Intent;i.type=0;end
        val allUrl = uri.toString()
        if (!isScheme(allUrl)) {
            return
        }
        //path -> /path
        uri.path?.let {
            val bundle = getBundleFromUrl(allUrl)
            toNextPage(navController, it.substringAfter("/"), bundle)
        }
    }

    private fun isScheme(url: String?): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val uri = Uri.parse(url)
        val scheme = uri.scheme
        val host = uri.host
        return TextUtils.equals(
            SchemeConstant.SCHEME,
            scheme
        ) && TextUtils.equals(SchemeConstant.HOST, host)
    }

    private fun toNextPage(navController: NavController, path: String, bundle: Bundle?) {
        try {
            Router.getInstance().build(path).with(bundle).navigation(navController)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getBundleFromUrl(url: String): Bundle? {
        //url -> scheme://host/path?scheme_content=intent:#Intent;i.type=0;end
        if (TextUtils.isEmpty(url)) {
            return Bundle()
        }
        var bundle: Bundle?
        val schemeContent = url.substringAfter("${KEY_SCHEME_CONTENT}=")
        //schemeContent -> intent:#Intent;B.isShow=true;S.name=tom;i.index=1;d.money=58.8;f.price=18.8;end
        try {
            val intent = Intent.parseUri(schemeContent, Intent.URI_INTENT_SCHEME)
            bundle = intent.extras
        } catch (e: java.lang.Exception) {
            bundle = Bundle()
        }
        return bundle
    }
}