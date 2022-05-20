package com.itant.md.ui.setting.help

import android.content.Context
import com.itant.md.R
import com.itant.mvp.kt.extension.launchTask
import com.miekir.common.widget.loading.LoadingType
import com.miekir.mvp.presenter.BasePresenter
import java.io.InputStreamReader

/**
 * @date 2022-5-13 22:54
 * @author 詹子聪
 */
class HelpPresenter: BasePresenter<HelpActivity>() {

    /**
     * 读取文件字符串
     */
    fun loadHelpFile(context: Context) {
        launchTask(
            {
                InputStreamReader(context.resources.openRawResource(R.raw.help)).use {
                    return@launchTask it.readText()
                }
            }, onSuccess = {
                view?.onTextLoad(it)
            }, loadingType = LoadingType.INVISIBLE
        )
    }
}