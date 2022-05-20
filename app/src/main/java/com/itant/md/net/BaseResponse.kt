package com.itant.md.net

import androidx.annotation.Keep
import com.miekir.task.net.IResponse

/**
 * 统一返回封装
 */
@Keep
class BaseResponse<T> : IResponse {
    /**
     * 返回状态代码
     */
    var code = NetConstants.SUCCESS

    /**
     * 消息
     */
    var msg: String? = null

    /**
     * 返回的实体数据
     */
    var data: T? = null

    companion object {
        @Transient
        private var sLastJumpMillis = 0L
    }

    override fun getServerMessage(): String? {
        return msg
    }

    /**
     * 校验返回信息，如果没有错误也没有过期就返回true，如果有错误则抛出异常即可
     * 防止有些不需要使用到结果的接口不断提交失败，及时发现隐藏的重大错误如登录过期等
     */
    override fun valid(): Boolean {
        if (code != NetConstants.SUCCESS) {
            /*if (code == NetConstants.EXPIRED && (System.currentTimeMillis()-sLastJumpMillis) > 2000L) {
                //ToastUtils.showShort("请重新登录")
                val topActivity = ActivityUtils.getTopActivity()
                if (topActivity != null) {
                    if (topActivity !is LoginActivity) {
                        sLastJumpMillis = System.currentTimeMillis()
                        val intent = Intent(GlobalContext.getContext(), LoginActivity::class.java)
                        intent.putExtra(LoginActivity.KEY_EXPIRED, true)
                        topActivity.startActivity(intent)
                        if (topActivity !is MachineActivity) {
                            topActivity.finish()
                        }
                    }
                } else {
                    sLastJumpMillis = System.currentTimeMillis()
                    val intent = Intent(GlobalContext.getContext(), LoginActivity::class.java)
                    intent.putExtra(LoginActivity.KEY_EXPIRED, true)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP
                    GlobalContext.getContext().startActivity(intent)
                }
            }
            throw ExceptionResult(code, msg)*/
        }
        return true
    }
}