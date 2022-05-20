package com.itant.md.ui.share.impl.pdf

import android.content.Context
import android.content.Intent
import com.itant.md.bean.NoteBean
import com.itant.md.ui.share.AbstractShare

/**
 * 分享为PDF
 * @date 2022-5-14 12:19
 * @author 詹子聪
 */
class SharePdf(context: Context, noteBean: NoteBean) : AbstractShare(context, noteBean) {
    override fun run() {
        context.startActivity(Intent(context, SharePdfActivity::class.java))
    }
}