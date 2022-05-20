package com.itant.md.ui.share.impl.image

import android.content.Context
import android.content.Intent
import com.itant.md.bean.NoteBean
import com.itant.md.ui.share.AbstractShare

/**
 * 分享为图片
 * @date 2022-5-14 12:19
 * @author 詹子聪
 */
class ShareImage(context: Context, noteBean: NoteBean) : AbstractShare(context, noteBean) {
    override fun run() {
        context.startActivity(Intent(context, ShareImageActivity::class.java))
    }
}