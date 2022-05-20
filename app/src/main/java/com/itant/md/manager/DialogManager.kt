package com.itant.md.manager

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import com.itant.md.R
import com.itant.md.bean.NoteBean
import com.itant.md.ext.getString
import com.itant.md.ext.showAdapt
import com.itant.md.ui.home.HomeActivity
import com.itant.mvp.kt.extension.setSingleClick

/**
 * @date 2022-5-14 12:00
 * @author 詹子聪
 */
object DialogManager {
    /**
     * 弹出命名对话框
     */
    fun showEditNameDialog(activity: Activity, oldTitle: String? = "", onNameBack: (newName: String) -> Unit) {
        AlertDialog.Builder(activity, R.style.AdaptAlertDialog).create().run {
            showAdapt()
            setContentView(R.layout.dialog_input_title)
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            findViewById<View>(R.id.tv_cancel).setSingleClick {
                dismiss()
            }
            val et_title = findViewById<EditText>(R.id.et_title)
            et_title.setText(oldTitle)
            et_title.setSelection(et_title.text.toString().length)
            findViewById<View>(R.id.tv_confirm).setSingleClick {
                val title = et_title.getString(activity.getString(R.string.input_title)) ?: return@setSingleClick
                dismiss()
                onNameBack.invoke(title)
            }
        }
    }

    /**
     * 一键导出文档对话框
     */
    fun exportDialog(activity: Activity, noteList: List<NoteBean>, onConfirm: (() -> Unit)? = null) {
        AlertDialog.Builder(activity, R.style.AdaptAlertDialog)
            .setMessage(activity.getString(R.string.msg_export, SettingManager.exportPath))
            .setNegativeButton(activity.getString(R.string.cancel), DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .setNeutralButton(activity.getString(R.string.clean), DialogInterface.OnClickListener { dialog, which ->
                (activity as? HomeActivity)?.mHomePresenter?.cleanDir()
            })
            .setPositiveButton(activity.getString(R.string.confirm), DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                onConfirm?.invoke()
                (activity as? HomeActivity)?.mHomePresenter?.exportAllNote(noteList)
            })
            .create().showAdapt()
    }

    /**
     * 一键导入文档对话框
     */
    fun importDialog(activity: Activity, onConfirm: (() -> Unit)? = null) {
        AlertDialog.Builder(activity, R.style.AdaptAlertDialog)
            .setMessage(activity.getString(R.string.msg_import, SettingManager.exportPath))
            .setNegativeButton(activity.getString(R.string.cancel), DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .setPositiveButton(activity.getString(R.string.confirm), DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                onConfirm?.invoke()
                (activity as? HomeActivity)?.mHomePresenter?.importAllNote()
            })
            .create().showAdapt()
    }

    /**
     * 删除日志对话框
     * @param onDeleted 删除成功的回调
     */
    fun showDeleteNoteDialog(activity: Activity, noteBean: NoteBean, onDeleted: (() -> Unit)? = null) {
        AlertDialog.Builder(activity, R.style.AdaptAlertDialog)
            .setMessage(activity.getString(R.string.msg_del, noteBean.noteTitle))
            .setNegativeButton(activity.getString(R.string.cancel), DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .setPositiveButton(activity.getString(R.string.confirm), DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                NoteManager.deleteLocal(noteBean)
                onDeleted?.invoke()
            })
            .create().showAdapt()
    }
}