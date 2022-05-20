package com.itant.md.ui.home

import android.text.TextUtils
import com.blankj.utilcode.util.ToastUtils
import com.itant.md.R
import com.itant.md.bean.NoteBean
import com.itant.md.manager.NoteManager
import com.itant.md.manager.SettingManager
import com.itant.md.utils.ShareUtils
import com.itant.mvp.kt.extension.launchTask
import com.miekir.common.context.GlobalContext
import com.miekir.common.widget.loading.LoadingType
import com.miekir.mvp.presenter.BasePresenter
import java.io.File

/**
 * 首页的主持人
 */
class HomePresenter: BasePresenter<HomeActivity>() {

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
                        ToastUtils.showShort(getString(R.string.export_success))
                    } else {
                        ToastUtils.showShort(message)
                    }
                }
            }, loadingType = LoadingType.STICKY
        )
    }

    /**
     * 一键导入日志
     */
    fun importAllNote() {
        val dir = File(SettingManager.exportPath)
        dir.mkdirs()
        if (!dir.exists()) {
            ToastUtils.showShort(GlobalContext.getContext().getString(R.string.dir_not_found))
            return
        }
        launchTask(
            {
                val fileList = dir.listFiles { dir, name ->
                    return@listFiles name.endsWith(".md")
                }

                if (fileList != null) {
                    for (noteFile in fileList) {
                        NoteManager.createOrUpdateLocal(
                            NoteBean().apply {
                                createTimeMillis = System.currentTimeMillis()
                                updateTimeMillis = createTimeMillis
                                noteTitle = noteFile.nameWithoutExtension
                                content = ShareUtils.readStringFromFile(noteFile)
                            }
                        )
                    }
                }
            }, onResult = { success, result, code, message ->
                view?.run {
                    if (success) {
                        ToastUtils.showShort(getString(R.string.import_success))
                        mSearchMode = false
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

    /**
     * 清理导出目录
     */
    fun cleanDir() {
        val dir = File(SettingManager.exportPath)
        if (!dir.exists()) {
            dir.mkdirs()
            ToastUtils.showShort(GlobalContext.getContext().getString(R.string.clean_success))
            return
        }
        launchTask(
            {
                val fileList = dir.listFiles()
                if (fileList != null) {
                    for (file in fileList) {
                        if (file.isFile) {
                            file.delete()
                        }
                    }
                }
            }, onResult = { success, result, code, message ->
                if (success) {
                    ToastUtils.showShort(GlobalContext.getContext().getString(R.string.clean_success))
                } else {
                    ToastUtils.showShort(message)
                }
            }, loadingType = LoadingType.STICKY
        )
    }
}