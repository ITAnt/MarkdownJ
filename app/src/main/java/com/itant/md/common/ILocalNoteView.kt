package com.itant.md.common

import com.miekir.mvp.view.base.IView

interface ILocalNoteView: IView {
    fun onSaveLocalNote(success: Boolean, result: Boolean, code: Int, message: String?)
}