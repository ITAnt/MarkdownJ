package com.itant.md.ui.share.impl.html

import android.text.TextUtils
import android.webkit.WebView
import com.blankj.utilcode.util.ToastUtils
import com.itant.md.R
import com.itant.md.databinding.ActivityShareDocBinding
import com.itant.md.ui.share.ShareActivity
import com.itant.mvp.kt.extension.lazy

open class ShareHtmlActivity: ShareActivity() {
    private val mHtmlPresenter: HtmlPresenter by lazy()

    override fun onBindingInflate() = ActivityShareDocBinding.inflate(layoutInflater)

    override fun onInit() {
        if (TextUtils.isEmpty(mNoteBean?.content)) {
            ToastUtils.showShort(getString(R.string.content_empty))
            finish()
        } else {
            WebView.enableSlowWholeDocumentDraw()
            mHtmlPresenter.convertMdToHtml(mNoteBean!!.content)
        }
    }

    /**
     * 转换为HTML成功
     */
    open fun onHtmlStringReady(result: String) {
        mHtmlPresenter.saveHtmlFile(result)
    }
}