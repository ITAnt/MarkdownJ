package com.itant.md.ui.home

import android.content.Intent
import android.text.TextUtils
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
import com.itant.md.databinding.ActivityHomeBinding
import com.itant.md.manager.DialogManager
import com.itant.md.ui.add.AddActivity
import com.itant.md.ui.setting.SettingActivity
import com.itant.mvp.kt.extension.lazy
import com.itant.mvp.kt.extension.openActivity
import com.itant.mvp.kt.extension.openActivityForResult
import com.itant.mvp.kt.extension.setSingleClick
import com.miekir.mvp.view.result.ActivityResult


/**
 * 首页
 */
class HomeActivity : BaseActivity<ActivityHomeBinding>(), ILocalNoteView {

    private val mList = ArrayList<NoteBean>()
    private val mAdapter = HomeAdapter(this, mList)
    val mHomePresenter: HomePresenter by lazy()
    val mLocalNotePresenter: LocalNotePresenter by lazy()

    /**
     * 关键字
     */
    private var mKeywords: String? = ""
    /**
     * 是否搜索模式
     */
    var mSearchMode = false
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

    override fun onBindingInflate() = ActivityHomeBinding.inflate(layoutInflater)

    override fun onInit() {
        binding.etSearch.addTextChangedListener (afterTextChanged = {
            mKeywords = it?.toString()
            if (!TextUtils.isEmpty(mKeywords)) {
                mHomePresenter.searchNote(mKeywords!!)
            }
        })

        // 导航栏颜色
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white_bg_3)
        supportActionBar?.run { title = "Markdown·J" }

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
            binding.etSearch.setText("")
        }
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

            // 一键导出
            R.id.action_export -> {
                DialogManager.exportDialog(this, mList)
            }

            // 一键导入
            R.id.action_import -> {
                DialogManager.importDialog(this)
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
            val newIntent = Intent(this@HomeActivity, AddActivity::class.java)
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
            ToastUtils.showShort(getString(R.string.rename_success))
            refreshList()
        } else {
            ToastUtils.showShort(message)
        }
    }
}