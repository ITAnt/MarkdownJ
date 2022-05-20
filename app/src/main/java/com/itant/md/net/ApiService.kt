package com.itant.md.net

import retrofit2.http.GET
import retrofit2.http.Query


/**
 * 请求接口
 * @date 2021-8-7 11:36
 * @author 詹子聪
 */
interface ApiService {

    /**
     * 获取验证码接口
     */
    @GET("prod-api/api/login/smsCode")
    suspend fun sendSmsCode(@Query("phone") phone: String): BaseResponse<Any>


}