package com.itant.md.ui.share.impl.html

import android.text.TextUtils
import com.blankj.utilcode.util.ToastUtils
import com.itant.md.R
import com.itant.md.manager.SettingManager
import com.itant.md.ui.share.ShareActivity
import com.itant.md.utils.ShareUtils
import com.itant.mvp.kt.extension.launchTask
import com.miekir.common.widget.loading.LoadingType
import com.miekir.mvp.presenter.BasePresenter
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.File

class HtmlPresenter: BasePresenter<ShareHtmlActivity>() {

    /**
     * MD文件内容转HTML
     */
    fun convertMdToHtml(content: String) {
        launchTask(
            {
                val parser: Parser = Parser.builder().build()
                val document: Node = parser.parse(content)
                val renderer = HtmlRenderer.builder().build()
                renderer.render(document)

                //AndDown().markdownToHtml(content)
            }, onResult = { success, result, code, message ->
                view?.run {
                    if (success && !TextUtils.isEmpty(result)) {
                        onHtmlStringReady(result!!)
                    } else {
                        ToastUtils.showShort(getString(R.string.convert_html_failed))
                        finish()
                    }
                }
            }, loadingType = LoadingType.INVISIBLE
        )
    }

    /**
     * 存储为HTML文件
     */
    fun saveHtmlFile(htmlContent: String) {
        val targetFile = File(SettingManager.exportPath, "${ShareActivity.mNoteBean!!.noteTitle}.html")
        launchTask(
            {
                ShareUtils.writeStringToFile(targetFile, htmlContent)
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