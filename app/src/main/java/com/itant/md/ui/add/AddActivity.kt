package com.itant.md.ui.add

import android.app.Activity
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.itant.md.R
import com.itant.md.base.BaseActivity
import com.itant.md.bean.NoteBean
import com.itant.md.common.ILocalNoteView
import com.itant.md.common.LocalNotePresenter
import com.itant.md.databinding.ActivityAddBinding
import com.itant.md.ext.newMarkwon
import com.itant.md.manager.DialogManager
import com.itant.md.manager.SettingManager
import com.itant.md.ui.add.action.bindAction
import com.itant.md.ui.add.action.getCurrentLineString
import com.itant.md.ui.setting.help.HelpActivity
import com.itant.md.ui.share.impl.html.ShareHtml
import com.itant.md.ui.share.impl.image.ShareImage
import com.itant.md.ui.share.impl.md.ShareMarkdown
import com.itant.md.ui.share.impl.pdf.SharePdf
import com.itant.md.view.TextViewUndoRedo
import com.itant.mvp.kt.extension.lazy
import com.itant.mvp.kt.extension.openActivity
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import io.noties.markwon.utils.NoCopySpannableFactory
import java.util.concurrent.Executors


/**
 * todo 暂时不支持暗黑主题，和markdown预览有冲突
 * 新增笔记界面
 * @date 2022-5-8 18:43
 * @author 詹子聪
 */
class AddActivity: BaseActivity<ActivityAddBinding>(), ILocalNoteView {

    companion object {
        const val KEY_TITLE = "key_title"
        var currentNote: NoteBean? = null
    }

    private lateinit var mUndoRedo: TextViewUndoRedo

    private lateinit var mMenuUndo: MenuItem
    private lateinit var mMenuRedo: MenuItem
    private lateinit var mMenuPreview: MenuItem
    private lateinit var mMenuEdit: MenuItem
    private lateinit var mMenuMore: MenuItem
    private lateinit var mMenuLivePreview: MenuItem

    private lateinit var mLivePreviewMark: Markwon
    private lateinit var mPreviewMark: Markwon

    /**
     * 内容是否已发生改变
     */
    private var mContentChanged = false
    private val mLocalNotePresenter: LocalNotePresenter by lazy()

    /**
     * 是否要退出
     */
    private var mShouldFinish = false

    /**
     * 是否为编辑模式，ActionBar图标随模式变化
     */
    private var mIsEditMode = true
        set(value) {
            field = value
            mMenuUndo.isVisible = value
            mMenuRedo.isVisible = value
            mMenuPreview.isVisible = value
            mMenuEdit.isVisible = !value
            binding.etContent.visibility = if (value) View.VISIBLE else View.GONE
            binding.tvLivePreview.visibility = if (SettingManager.livePreviewEnable && value && !TextUtils.isEmpty(binding.etContent.getCurrentLineString())) View.VISIBLE else View.GONE
            binding.svPreview.run {
                if (value) {
                    // 编辑模式隐藏预览
                    visibility = View.GONE
                } else {
                    // 非编辑模式则展示预览
                    visibility = View.VISIBLE
                    mPreviewMark.setMarkdown(binding.tvPreview, binding.etContent.text.toString())
                }
            }
            if (value) {
                binding.etContent.run { post { requestFocus() } }
                binding.etContent.setSelection(binding.etContent.text.toString().length)
                KeyboardUtils.showSoftInput(binding.etContent)
                if (SettingManager.quickInputEnable) {
                    binding.svQuick.visibility = View.VISIBLE
                }
            } else {
                binding.svQuick.visibility = View.GONE
                KeyboardUtils.hideSoftInput(binding.etContent)
            }
        }

    override fun onBindingInflate() = ActivityAddBinding.inflate(layoutInflater)

    override fun onInit() {
        mPreviewMark = binding.tvPreview.newMarkwon().build()
        mLivePreviewMark = binding.tvLivePreview.newMarkwon().build()

        // 语法高亮
        if (SettingManager.highlightEnable) {
            MarkwonEditor.create(mPreviewMark).run {
                binding.etContent.addTextChangedListener(MarkwonEditorTextWatcher.withPreRender(
                    this,
                    Executors.newCachedThreadPool(),
                    binding.etContent
                ))
            }
        }

        // 手动点击关闭输入法才关闭
        touchSpaceHideKeyboard = false

        mUndoRedo = TextViewUndoRedo(binding.etContent)
        supportActionBar?.run {
            // 显示返回箭头
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            // 初始化标题
            currentNote?.run { title = noteTitle }
        }

        // 光标移动时，渲染预览
        binding.etContent.setCursorMoveListener { renderLivePreview() }

        // TextView需要展示多次markdown用这个可以提高效率
        binding.tvLivePreview.setSpannableFactory(NoCopySpannableFactory.getInstance())

        // 监听快捷输入
        binding.bindAction(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        menu?.run {
            mMenuUndo = findItem(R.id.action_undo)
            mMenuRedo = findItem(R.id.action_redo)
            mMenuPreview = findItem(R.id.action_preview)
            mMenuEdit = findItem(R.id.action_edit)
            mMenuMore = findItem(R.id.action_more)
            mMenuLivePreview = findItem(R.id.action_live_preview)
            findItem(R.id.action_help).isVisible = SettingManager.isSimpleChinese()
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // 保存日志并退出
            android.R.id.home -> onBackPressed()
            // 编辑模式
            R.id.action_edit -> mIsEditMode = true
            // 立即保存到本地
            R.id.menu_save -> currentNote?.run { mLocalNotePresenter.saveNote(this) }
            // 预览模式
            R.id.action_preview -> mIsEditMode = false
            // 撤销操作
            R.id.action_undo -> mUndoRedo.undo()
            // 取消撤销
            R.id.action_redo -> mUndoRedo.redo()
            // 编辑标题
            R.id.menu_edit_title -> editTitle()
            // 删除文档
            R.id.menu_del -> deleteNote()
            // 实时预览
            R.id.action_live_preview -> {
                SettingManager.livePreviewEnable = !SettingManager.livePreviewEnable
                renderLivePreview()
            }
            // 语法帮助
            R.id.action_help -> openActivity<HelpActivity>()
            // 分享为PDF
            R.id.share_pdf -> currentNote?.let { SharePdf(this, it).run() }
            // 分享为MD文件
            R.id.share_md -> currentNote?.let { ShareMarkdown(this, it).run() }
            // 分享为HTML文件
            R.id.share_html -> currentNote?.let { ShareHtml(this, it).run() }
            // 分享为图片
            R.id.share_image -> currentNote?.let { ShareImage(this, it).run() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus && mIsFirstLoad) {
            if (currentNote == null) {
                // 如果是新建则自动弹出软键盘
                mContentChanged = true
                val newTitle = intent.getStringExtra(KEY_TITLE)!!
                currentNote = NoteBean().apply {
                    noteTitle = newTitle
                    createTimeMillis = System.currentTimeMillis()
                    updateTimeMillis = createTimeMillis
                }
                supportActionBar?.title = newTitle
                mIsEditMode = true
            } else {
                // 如果是从item点进来，内容为空则是编辑模式，内容不为空则是预览模式
                binding.etContent.setText(currentNote!!.content)
                mIsEditMode = TextUtils.isEmpty(currentNote!!.content)
            }

            // 动态改变撤销、前进按钮颜色
            binding.etContent.addTextChangedListener(afterTextChanged = {
                mContentChanged = true
                mMenuUndo.icon?.run {
                    mutate()
                    colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this@AddActivity, if (mUndoRedo.canUndo) R.color.theme_color_title else R.color.gray_text3), PorterDuff.Mode.SRC_ATOP)
                }

                mMenuRedo.icon?.run {
                    mutate()
                    colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this@AddActivity, if (mUndoRedo.canRedo) R.color.theme_color_title else R.color.gray_text3), PorterDuff.Mode.SRC_ATOP)
                }
                it?.run { currentNote?.content = toString() }
            })
        }
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onDestroy() {
        super.onDestroy()
        currentNote = null
    }

    /**
     * 按返回键时，如果内容不为空则保存笔记
     */
    override fun onBackPressed() {
        if (!mContentChanged) {
            finish()
            return
        }
        mShouldFinish = true
        mLocalNotePresenter.saveNote(currentNote!!)
    }

    /**
     * 修改标题
     */
    private fun editTitle() {
        DialogManager.showEditNameDialog(this, currentNote?.noteTitle) {
            mContentChanged = true
            currentNote?.run {
                noteTitle = it
                supportActionBar?.run { title = it }
                mLocalNotePresenter.saveNote(this)
            }
        }
    }

    /**
     * 实时预览
     */
    private fun renderLivePreview() {
        if (SettingManager.livePreviewEnable) {
            val lineText = binding.etContent.getCurrentLineString().toString()
            if (!TextUtils.isEmpty(lineText)) {
                binding.tvLivePreview.visibility = View.VISIBLE
                mLivePreviewMark.setMarkdown(binding.tvLivePreview, lineText)
            } else {
                binding.tvLivePreview.visibility = View.GONE
            }
        } else {
            binding.tvLivePreview.visibility = View.GONE
        }
    }

    /**
     * 删除文档
     */
    private fun deleteNote() {
        currentNote?.run {
            DialogManager.showDeleteNoteDialog(this@AddActivity, this) {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onSaveLocalNote(success: Boolean, result: Boolean, code: Int, message: String?) {
        if (result) {
            // 成功
            setResult(Activity.RESULT_OK, intent)
            if (mShouldFinish) {finish()}
        } else {
            // 失败
            ToastUtils.showShort(message)
            mShouldFinish = false
        }
    }
}