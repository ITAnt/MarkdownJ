package com.itant.md.ui.home.vip

import android.text.TextUtils
import com.blankj.utilcode.util.ToastUtils
import com.itant.md.bean.NoteBean
import com.itant.md.manager.NoteManager
import com.itant.md.manager.SettingManager
import com.itant.md.utils.ShareUtils
import com.itant.mvp.kt.extension.launchTask
import com.miekir.common.widget.loading.LoadingType
import com.miekir.mvp.presenter.BasePresenter
import java.io.File

/**
 * 首页的主持人
 */
class VipHomePresenter: BasePresenter<VipHomeActivity>() {

    /**
     * 获取本地日志
     */
    fun getNoteList() {
        launchTask(
            {
                NoteManager.getLocalList()
            }, onSuccess = {
                view?.onLocalNoteList(it!!)
            }, loadingType = LoadingType.INVISIBLE
        )
    }

    /**
     * 一键导出日志
     */
    fun exportAllNote(noteList: List<NoteBean>) {
        launchTask(
            {
                for (note in noteList) {
                    val targetFile = File(SettingManager.exportPath, "${note.noteTitle}.md")
                    ShareUtils.writeStringToFile(targetFile, note.content)
                }
            }, onResult = { success, result, code, message ->
                view?.run {
                    if (success) {
                        ToastUtils.showShort("导出成功")
                    } else {
                        ToastUtils.showShort(message)
                    }
                }
            }, loadingType = LoadingType.STICKY
        )
    }

    /**
     * 搜索日志
     */
    fun searchNote(keywords: String?) {
        if (TextUtils.isEmpty(keywords)) {
            return
        }
        launchTask(
            {
                val list = ArrayList<NoteBean>()
                val allList = NoteManager.getLocalList()
                for (note in allList) {
                    if (note.content.contains(keywords!!) || note.noteTitle.contains(keywords)) {
                        list.add(note)
                    }
                }
                list
            }, onSuccess = {
                view?.onLocalNoteList(it!!)
            }, loadingType = LoadingType.INVISIBLE
        )
    }
}