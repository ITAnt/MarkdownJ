package com.itant.md.ui.share.impl.html

import android.content.Context
import android.content.Intent
import com.itant.md.bean.NoteBean
import com.itant.md.ui.share.AbstractShare


/**
 * 分享为HTML文件
 * @date 2022-5-14 12:19
 * @author 詹子聪
 */
class ShareHtml(context: Context, noteBean: NoteBean) : AbstractShare(context, noteBean) {
    override fun run() {
        context.startActivity(Intent(context, ShareHtmlActivity::class.java))
    }
}