package com.itant.md.ui.home.vip

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.itant.md.R
import com.itant.md.base.BaseActivity
import com.itant.md.bean.NoteBean
import com.itant.md.common.ILocalNoteView
import com.itant.md.common.LocalNotePresenter
import com.itant.md.databinding.ActivityHomeVipBinding
import com.itant.md.ext.showAdapt
import com.itant.md.manager.DialogManager
import com.itant.md.manager.SettingManager
import com.itant.md.ui.add.AddActivity
import com.itant.md.ui.setting.SettingActivity
import com.itant.md.view.CoverDrawable
import com.itant.mvp.kt.extension.*
import com.miekir.mvp.view.result.ActivityResult


/**
 * 首页
 */
class VipHomeActivity : BaseActivity<ActivityHomeVipBinding>(), ILocalNoteView {

    private val mList = ArrayList<NoteBean>()
    private val mAdapter = VipHomeAdapter(this, mList)
    val mHomePresenter: VipHomePresenter by lazy()
    val mLocalNotePresenter: LocalNotePresenter by lazy()

    /**
     * 关键字
     */
    private var mKeywords: String? = ""
    /**
     * 是否搜索模式
     */
    private var mSearchMode = false
        set(value) {
            field = value
            if (value) {
                binding.viewSearch.visibility = View.VISIBLE
                if (!TextUtils.isEmpty(mKeywords)) {
                    mHomePresenter.searchNote(mKeywords!!)
                }
            } else {
                binding.viewSearch.visibility = View.GONE
                mHomePresenter.getNoteList()
            }
        }

    override fun onBindingInflate() = ActivityHomeVipBinding.inflate(layoutInflater)

    override fun onInit() {
        // 导航栏颜色
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white_bg_3)

        binding.etSearch.addTextChangedListener (afterTextChanged = {
            mKeywords = it?.toString()
            if (!TextUtils.isEmpty(mKeywords)) {
                mHomePresenter.searchNote(mKeywords!!)
            }
        })

        supportActionBar?.run { title = "M·J" }

        binding.rvContent.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        binding.fabAdd.setSingleClick {
            newNote()
        }

        // 先加载本地的日志，再加载服务器的
        mHomePresenter.getNoteList()

        binding.ivClose.setSingleClick {
            mSearchMode = false
        }

        binding.viewBottom.background = CoverDrawable(
            ColorDrawable(ContextCompat.getColor(this, R.color.white_bg_3)),
            TypedValue.applyDimension(COMPLEX_UNIT_DIP, 319.0f, resources.displayMetrics).toInt(),
            0,
            TypedValue.applyDimension(COMPLEX_UNIT_DIP, 30.0f, resources.displayMetrics).toInt()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            // 新建笔记
            R.id.action_add -> newNote()

            // 设置
            R.id.action_setting -> openActivity<SettingActivity>()

            R.id.action_import -> {
                /*// 必须申请这个权限，否则没有权限获取非媒体文件
                if (Build.VERSION.SDK_INT >= 30 && !Environment.isExternalStorageManager()) {
                    ToastUtils.showShort(getString(R.string.read_permission))
                    binding.rvContent.postDelayed({
                        try {
                            // 为当前APP申请访问所有文件的权限，仅展示当前APP
                            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, Uri.parse("package:$packageName"))
                            startActivity(intent)
                        } catch (e: Exception) {
                            // 为当前APP申请访问所有文件的权限，自行在APP列表选择
                            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                            try {
                                startActivity(intent)
                            } catch (ex: java.lang.Exception) {
                                L.e(ex.message)
                            }
                        }
                    }, 1_500)
                } else {
                    requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, callback = {granted, temp ->
                        if (granted) {
                            DialogManager.importDialog(this)
                        } else {
                            ToastUtils.showShort(getString(R.string.read_permission))
                        }
                    })
                }*/
            }

            // 一键导出
            R.id.action_export -> {
                requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, callback = {granted, temp ->
                    if (granted) {
                        exportDialog(mList)
                    } else {
                        ToastUtils.showShort("请授予写文件权限")
                    }
                })
            }

            // 搜索模式
            R.id.action_search -> {
                mSearchMode = !mSearchMode
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 写日志
     */
    private fun newNote() {
        DialogManager.showEditNameDialog(this) {
            val newIntent = Intent(this@VipHomeActivity, AddActivity::class.java)
            newIntent.putExtra(AddActivity.KEY_TITLE, it)
            openActivityForResult(newIntent, object: ActivityResult() {
                override fun onResultOK(backIntent: Intent?) {
                    super.onResultOK(backIntent)
                    refreshList()
                }
            })
        }
    }

    /**
     * 刷新列表
     */
    fun refreshList() {
        if (mSearchMode) {
            mHomePresenter.searchNote(mKeywords)
        } else {
            mHomePresenter.getNoteList()
        }
    }

    /**
     * 成功获取本地日志
     */
    fun onLocalNoteList(noteList: List<NoteBean>) {
        mList.clear()
        mList.addAll(noteList)
        mAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (mSearchMode) {
            mSearchMode = false
            return
        }
        ActivityUtils.startHomeActivity()
    }

    override fun onSaveLocalNote(success: Boolean, result: Boolean, code: Int, message: String?) {
        if (success) {
            ToastUtils.showShort("重命名成功")
            refreshList()
        } else {
            ToastUtils.showShort(message)
        }
    }

    /**
     * 一键导出文档对话框
     */
    private fun exportDialog(noteList: List<NoteBean>, onConfirm: (() -> Unit)? = null) {
        AlertDialog.Builder(this, R.style.AdaptAlertDialog)
            .setMessage("一键导出到${SettingManager.exportPath}文件夹，将覆盖已存在的同名文档。")
            .setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, which ->
                onConfirm?.invoke()
                mHomePresenter.exportAllNote(noteList)
            })
            .create().showAdapt()
    }
}