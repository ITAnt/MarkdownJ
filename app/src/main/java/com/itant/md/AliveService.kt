package com.itant.md

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import com.blankj.utilcode.util.AppUtils

/**
 * 1.提高存活优先级；
 * 2.在APP被系统回收时彻底杀死进程，防止onRestoreInstanceState（因为非常缓慢卡顿）
 */
class AliveService : Service() {
    private val mBinder = MyBinder()

    inner class MyBinder : Binder() {
        val service: AliveService
            get() = this@AliveService
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppUtils.exitApp()
    }

    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }
}