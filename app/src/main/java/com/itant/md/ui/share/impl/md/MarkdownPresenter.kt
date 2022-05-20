package com.itant.md.ui.share.impl.md

import com.blankj.utilcode.util.ToastUtils
import com.itant.md.manager.SettingManager
import com.itant.md.ui.share.ShareActivity
import com.itant.md.utils.ShareUtils
import com.itant.mvp.kt.extension.launchTask
import com.miekir.common.widget.loading.LoadingType
import com.miekir.mvp.presenter.BasePresenter
import java.io.File

/**
 * @date 2022-5-16 21:31
 * @author 詹子聪
 */
class MarkdownPresenter: BasePresenter<ShareMdActivity>() {

    /**
     * 存储为MD文件
     */
    fun saveMdFile() {
        val targetFile = File(SettingManager.exportPath, "${ShareActivity.mNoteBean!!.noteTitle}.md")
        launchTask(
            {
                ShareUtils.writeStringToFile(targetFile, ShareActivity.mNoteBean!!.content)
            }, onResult = { success, result, code, message ->
                view?.run {
                    if (result!!) {
                        ShareUtils.shareFile(this, targetFile)
                    } else {
                        ToastUtils.showShort(message)
                    }
                    finish()
                }
            }, loadingType = LoadingType.INVISIBLE
        )
    }
}