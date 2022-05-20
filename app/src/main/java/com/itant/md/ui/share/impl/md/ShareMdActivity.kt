package com.itant.md.ui.share.impl.md

import android.text.TextUtils
import com.blankj.utilcode.util.ToastUtils
import com.itant.md.R
import com.itant.md.ui.share.ShareActivity
import com.itant.mvp.kt.extension.lazy

/**
 * @date 2022-5-16 21:18
 * @author 詹子聪
 */
class ShareMdActivity: ShareActivity() {
    private val markdownPresenter: MarkdownPresenter by lazy()

    override fun onInit() {
        super.onInit()
        if (TextUtils.isEmpty(mNoteBean?.content)) {
            ToastUtils.showShort(getString(R.string.content_empty))
            finish()
        } else {
            markdownPresenter.saveMdFile()
        }
    }
}