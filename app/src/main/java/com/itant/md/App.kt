package com.itant.md

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.miekir.common.log.L
import com.miekir.mvp.view.anim.SlideAnimation
import com.miekir.task.MvpManager
import com.miekir.task.net.RetrofitManager
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tencent.mmkv.MMKV

/**
 * todo: 提供网页版浏览、编辑
 * @date 2021-8-28 15:26
 * @author 詹子聪
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        // 初始化本地存储
        val rootDir = MMKV.initialize(this)
        L.i("mmkv root: $rootDir")

        //val appKey = "62862bab88ccdf4b7e6ea943"
        //val channel = "GooglePlay"
        //UMConfigure.preInit(this, appKey, channel)
        //UMConfigure.init(this, appKey, channel, UMConfigure.DEVICE_TYPE_PHONE, "")

        // 应用主题
        //AppCompatDelegate.setDefaultNightMode(SettingManager.themeMode)
        ToastUtils.getDefaultMaker()
            .setBgColor(ContextCompat.getColor(this, R.color.orange_text))
            .setTextColor(ContextCompat.getColor(this, R.color.white))

        // MVP相关设置
        MvpManager.getInstance().activityAnimation(SlideAnimation())
        RetrofitManager.getDefault()
            .addInterceptors(ChuckInterceptor(this))
            .printLog(AppUtils.isAppDebug())

        // 启动一个线程：1.提高存活优先级；2.在APP被系统回收时彻底杀死进程，防止onRestoreInstanceState（因为非常缓慢卡顿）
        startService(Intent(this, AliveService::class.java))
    }
}