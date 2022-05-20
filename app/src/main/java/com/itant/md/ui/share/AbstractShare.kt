package com.itant.md.ui.share

import android.content.Context
import com.itant.md.bean.NoteBean
import com.itant.md.manager.NoteManager
import com.itant.md.storage.db.DbManager

/**
 * 分享的抽象
 * @date 2022-5-14 12:18
 * @author 詹子聪
 */
abstract class AbstractShare(val context: Context, val noteBean: NoteBean) {
    init {
        ShareActivity.mNoteBean = noteBean
        // 分享之前先临时保存一下
        DbManager.mService.submit {
            NoteManager.createOrUpdateLocal(noteBean)
        }
    }

    abstract fun run()
}