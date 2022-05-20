package com.itant.md.ui.setting.help

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.widget.NestedScrollView
import com.itant.md.R
import com.itant.md.base.BaseActivity
import com.itant.md.databinding.ActivitySettingHelpBinding
import com.itant.md.ext.newMarkwon
import com.itant.mvp.kt.extension.lazy


/**
 * 语法帮助界面
 * @date 2022-5-13 22:50
 * @author 詹子聪
 */
class HelpActivity: BaseActivity<ActivitySettingHelpBinding>() {

    /**
     * 原文高度
     */
    private var mContentHeight = -1
    private var mHalfContentHeight = 0

    /**
     * 预览高度
     */
    private var mPreviewHeight = -1

    /**
     * 是否触摸内容
     */
    private var mTouchContent = false
    private var mContentScrollY = 0

    /**
     * 是否触摸预览
     */
    private var mTouchPreview = false
    private var mPreviewScrollY = 0

    private val mPresenter: HelpPresenter by lazy()

    override fun onBindingInflate() = ActivitySettingHelpBinding.inflate(layoutInflater)

    override fun onInit() {
        supportActionBar?.run {
            // 显示返回箭头
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.help)
        }

        // 监听内容滚动
        binding.svContent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            mContentScrollY += (scrollY - oldScrollY)
            // 既按了内容，又按了预览，无法跟随滑动
            if (mTouchContent && mTouchPreview) {
                return@OnScrollChangeListener
            }
            if (mContentHeight == -1 || mPreviewHeight == -1 || !mTouchContent) {
                return@OnScrollChangeListener
            }
            // 折算比例
            val previewScrollY = ((mContentScrollY.toDouble() / mContentHeight) * mPreviewHeight).toInt()
            binding.svPreview.scrollTo(0, previewScrollY)
        })
        binding.svContent.setOnTouchListener { v, event ->
            if (mPreviewHeight < mHalfContentHeight) {
                mPreviewHeight = binding.tvPreview.height
            }
            mTouchContent = true
            mTouchPreview = false
            false
        }

        // 监听预览滚动
        binding.svPreview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            mPreviewScrollY += (scrollY - oldScrollY)
            // 既按了内容，又按了预览，无法跟随滑动
            if (mTouchContent && mTouchPreview) {
                return@OnScrollChangeListener
            }
            if (mContentHeight == -1 || mPreviewHeight == -1 || !mTouchPreview) {
                return@OnScrollChangeListener
            }
            // 折算比例
            val contentScrollY = ((mPreviewScrollY.toDouble() / mPreviewHeight) * mContentHeight).toInt()
            binding.svContent.scrollTo(0, contentScrollY)
        })

        binding.svPreview.setOnTouchListener { v, event ->
            if (mPreviewHeight < mHalfContentHeight) {
                mPreviewHeight = binding.tvPreview.height
            }
            mTouchContent = false
            mTouchPreview = true
            false
        }

        mPresenter.loadHelpFile(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_help, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            // 原文
            R.id.action_plain -> {
                binding.svContent.visibility = View.VISIBLE
                binding.svPreview.visibility = View.GONE
            }
            // 分割
            R.id.action_split -> {
                binding.svContent.visibility = View.VISIBLE
                binding.svPreview.visibility = View.VISIBLE
            }
            // 预览
            R.id.action_preview -> {
                binding.svContent.visibility = View.GONE
                binding.svPreview.visibility = View.VISIBLE
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onTextLoad(it: String?) {
        // 展示原文，测量真正高度
        binding.tvContent.text = it
        binding.tvContent.post {
            mContentHeight = binding.tvContent.height
            mHalfContentHeight = mContentHeight / 2
        }
        // 预览效果，测量真正高度
        binding.tvPreview.run {
            newMarkwon().build().setMarkdown(this, binding.tvContent.text.toString())
        }
    }
}