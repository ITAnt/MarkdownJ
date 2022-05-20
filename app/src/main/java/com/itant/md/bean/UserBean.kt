package com.itant.md.bean

import androidx.annotation.Keep

/**
 * @date 2022-5-9 22:17
 * @author 詹子聪
 */
@Keep
class UserBean {
    /**
     * 昵称
     */
    var nickname = ""

    /**
     * 邮箱
     */
    var email = ""

    /**
     * 是否为VIP todo：同步时，服务器端也要做校验
     */
    var isVip = false
}