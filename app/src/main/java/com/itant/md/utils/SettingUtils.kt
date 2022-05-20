package com.itant.md.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.ToastUtils
import com.itant.md.R
import com.miekir.common.context.GlobalContext
import java.net.URLEncoder


object SettingUtils {
    /**
     * 发邮件
     */
    fun sendEmail(activity: Activity) {
        try {
            val data = Intent(Intent.ACTION_SENDTO)
            data.data = Uri.parse("mailto:1046883121@qq.com")
            data.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.email_title))
            //data.putExtra(Intent.EXTRA_TEXT, "这是内容")
            activity.startActivity(data)
        } catch (e: Exception) {
            ToastUtils.showShort(activity.getString(R.string.email_not_found))
        }
    }

    /**
     * 捐款
     */
    fun donateMoneyWithAccount(context: Activity, account: Int) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri: Uri? = Uri.parse("alipays://platformapi/startapp?appId=20000123&actionType=scan&biz_data={\"s\": \"money\",\"u\": \"2088502886005194\",\"a\": \"${account}\",\"m\":\"爱心捐赠\"}")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = uri
            GlobalContext.getContext().startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            donateMoney(context)
        }
    }

    fun donateMoney(context: Activity, qrcode: String = "https://qr.alipay.com/fkx10132d0dbctzfayolc86") {
        try {
            val code = URLEncoder.encode(qrcode, "utf-8")
            val alipayqr = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=$code"
            openUrl(context, alipayqr + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis())
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort(context.getString(R.string.alipay_error))
        }

    }

    private fun openUrl(context: Activity, url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}