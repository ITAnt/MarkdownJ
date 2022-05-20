package com.itant.md.net

import com.itant.md.BuildConfig
import com.miekir.task.net.RetrofitManager

/**
 * 网络请求封装
 * @date 2021-8-7 11:32
 * @author 詹子聪
 */
object ApiManager {
    /**
     * 默认的网络请求
     */
    val default = RetrofitManager.getDefault().createApiService(BuildConfig.BASE_URL, ApiService::class.java)
}