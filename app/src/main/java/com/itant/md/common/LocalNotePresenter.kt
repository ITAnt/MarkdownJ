package com.itant.md.common

import com.itant.md.bean.NoteBean
import com.itant.md.manager.NoteManager
import com.itant.mvp.kt.extension.launchTask
import com.miekir.common.widget.loading.LoadingType
import com.miekir.mvp.presenter.BasePresenter

class LocalNotePresenter: BasePresenter<ILocalNoteView>() {
    /**
     * 保存日志
     */
    fun saveNote(noteBean: NoteBean) {
        launchTask(
            {
                NoteManager.createOrUpdateLocal(noteBean)
            }, onResult = { success, result, code, message ->
                view?.onSaveLocalNote(success, result!!, code, message)
            }, loadingType = LoadingType.INVISIBLE
        )
    }
}