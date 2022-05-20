package com.itant.md.ui.home.vip

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.itant.md.R
import com.itant.md.bean.NoteBean
import com.itant.md.ext.showAdapt
import com.itant.md.manager.DialogManager
import com.itant.md.ui.add.AddActivity
import com.itant.md.ui.share.impl.md.ShareMarkdown
import com.itant.mvp.kt.extension.openActivityForResult
import com.itant.mvp.kt.extension.setSingleClick
import com.miekir.mvp.view.result.ActivityResult

/**
 * 首页日志适配器
 */
class VipHomeAdapter(private val mContext: Context, val mList: MutableList<NoteBean>): BaseQuickAdapter<NoteBean, BaseViewHolder>(
    R.layout.item_note_vip, data = mList) {

    override fun convert(holder: BaseViewHolder, item: NoteBean) {
        val tv_title = holder.getView<TextView>(R.id.tv_title)
        tv_title.text = item.noteTitle

        val tv_content = holder.getView<TextView>(R.id.tv_content)
        tv_content.text = item.content

        val tv_create_time = holder.getView<TextView>(R.id.tv_create_time)
        tv_create_time.text = "创建：${TimeUtils.getString(item.createTimeMillis, 0L, TimeConstants.MSEC)}"

        val tv_update_time = holder.getView<TextView>(R.id.tv_update_time)
        tv_update_time.text = "${TimeUtils.getString(item.updateTimeMillis, 0L, TimeConstants.MSEC)}"

        val iv_status = holder.getView<ImageView>(R.id.iv_status)
        if (item.syncWithServer) {
            iv_status.setImageResource(R.drawable.ic_store_server)
        } else {
            iv_status.setImageResource(R.drawable.ic_store_local)
            //iv_status.setColorFilter(ContextCompat.getColor(context, R.color.gray_text3), PorterDuff.Mode.SRC_IN)
        }

        val cv_item = holder.getView<View>(R.id.cv_item)
        // 跳转预览
        cv_item.setSingleClick {
            AddActivity.currentNote = item
            (mContext as? VipHomeActivity)?.run {
                openActivityForResult(Intent(context, AddActivity::class.java), object: ActivityResult() {
                    override fun onResultOK(backIntent: Intent?) {
                        super.onResultOK(backIntent)
                        refreshList()
                    }
                })
            }
        }
        // 长按弹出菜单：1命名；2分享；3删除
        cv_item.setOnLongClickListener {
            (mContext as? VipHomeActivity)?.let { activity ->
                AlertDialog.Builder(activity, R.style.AdaptAlertDialog).create().run {
                    showAdapt()
                    setContentView(R.layout.dialog_note_item_action)
                    // 重命名
                    findViewById<View>(R.id.view_rename).setSingleClick {
                        dismiss()
                        showRenameDialog(activity, item)
                    }
                    // 直接保存为md文件并分享
                    findViewById<View>(R.id.view_share).setSingleClick {
                        dismiss()
                        ShareMarkdown(activity, item).run()
                    }
                    // 删除
                    findViewById<View>(R.id.view_del).setSingleClick {
                        dismiss()
                        showDeleteDialog(activity, item, holder.absoluteAdapterPosition)
                    }
                }
            }
            true
        }
    }

    /**
     * 重命名
     */
    private fun showRenameDialog(activity: VipHomeActivity, item: NoteBean) {
        DialogManager.showEditNameDialog(activity, item.noteTitle) {
            // 重命名
            item.noteTitle = it
            (mContext as? VipHomeActivity)?.run { mLocalNotePresenter.saveNote(item) }
        }
    }


    /**
     * 弹出确认删除对话框
     */
    private fun showDeleteDialog(activity: VipHomeActivity, item: NoteBean, position: Int) {
        DialogManager.showDeleteNoteDialog(activity, item) {
            // 删除成功之后，刷新列表
            mList.remove(item)
            notifyItemRemoved(position)
        }
    }
}