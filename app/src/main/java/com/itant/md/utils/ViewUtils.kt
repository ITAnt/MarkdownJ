package com.itant.md.utils

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * @date 2021-10-17 20:39
 * @author 詹子聪
 */
object ViewUtils {
    /**
     * 获取控件左上顶点 y 坐标
     * @param view
     * @return
     */
    fun getY(view: View): Int {
        val rect = IntArray(2)
        view.getLocationOnScreen(rect)
        return rect[1] //返回纵坐标
    }

    /**
     * 初始化WebView
     */
    fun initWebView(webView: WebView) {
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        // 设置允许JS弹窗
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        // 设置可以访问文件
        webSettings.allowFileAccess = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowContentAccess = true
        webSettings.domStorageEnabled = true
        webSettings.allowFileAccessFromFileURLs = true
        // 不使用缓存，只从网络获取数据
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.defaultTextEncodingName = "UTF-8"
        webSettings.domStorageEnabled = true
    }

    fun destroyWebView(webView: WebView?) {
        if (webView == null) {
            return
        }

        try {
            webView.run {
                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
                settings.javaScriptEnabled = false
                val parent: ViewParent = parent
                (parent as ViewGroup).removeView(this)
                stopLoading()
                removeAllViews()

                clearCache(true)
                clearFormData()
                clearHistory()
                clearSslPreferences()

                destroy()
            }

            // Clear all the cookies
            CookieManager.getInstance().removeSessionCookie()
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()

            // Clear all the Application Cache, Web SQL Database and the HTML5 Web Storage
            //WebStorage.getInstance().deleteAllData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}