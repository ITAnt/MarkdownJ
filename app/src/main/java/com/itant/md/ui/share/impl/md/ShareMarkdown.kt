package com.itant.md.ui.share.impl.md

import android.content.Context
import android.content.Intent
import com.itant.md.bean.NoteBean
import com.itant.md.ui.share.AbstractShare

/**
 * 分享markdown文件
 */
class ShareMarkdown(context: Context, noteBean: NoteBean) : AbstractShare(context, noteBean) {
    override fun run() {
        context.startActivity(Intent(context, ShareMdActivity::class.java))
    }
}