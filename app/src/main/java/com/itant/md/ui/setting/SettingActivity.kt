package com.itant.md.ui.setting

import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.itant.md.BuildConfig
import com.itant.md.R
import com.itant.md.base.BaseActivity
import com.itant.md.databinding.ActivitySettingBinding
import com.itant.md.manager.SettingManager
import com.itant.md.ui.setting.help.HelpActivity
import com.itant.md.utils.SettingUtils
import com.itant.mvp.kt.extension.openActivity
import com.itant.mvp.kt.extension.setSingleClick

/**
 * 设置界面
 */
class SettingActivity: BaseActivity<ActivitySettingBinding>() {
    override fun onBindingInflate() = ActivitySettingBinding.inflate(layoutInflater)

    override fun onInit() {
        // 显示返回箭头
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.app_setting)
        }

        // 切换主题
        if (SettingManager.isNightMode()) {
            binding.rbBlack.isChecked = true
        } else {
            binding.rbWhite.isChecked = true
        }
        binding.rgTheme.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding.rbBlack.id) {
                SettingManager.applyThemeMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                SettingManager.applyThemeMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            openActivity<SettingActivity>()
            overridePendingTransition(R.anim.anim_open, R.anim.anim_close)
            finish()
        }

        // 导出路径
        binding.tvPath.text = SettingManager.exportPath

        // 版本
        binding.tvVersion.text = "Markdown·J V${BuildConfig.VERSION_NAME}"

        // 语法帮助
        if (SettingManager.isSimpleChinese()) {
            binding.viewHelp.visibility = View.VISIBLE
            binding.viewHelp.setSingleClick {
                openActivity<HelpActivity>()
            }
        }

        // 邮件联系
        binding.viewEmail.setSingleClick {
            SettingUtils.sendEmail(this)
        }

        // 爱心捐赠
        binding.viewDonate.setSingleClick {
            SettingUtils.donateMoney(this)
        }

        // 实时预览
        binding.swLivePreview.isChecked = SettingManager.livePreviewEnable
        binding.swLivePreview.setOnCheckedChangeListener { _, isChecked ->
            SettingManager.livePreviewEnable = isChecked
        }
        // 语法高亮
        binding.swHighlight.isChecked = SettingManager.highlightEnable
        binding.swHighlight.setOnCheckedChangeListener { _, isChecked ->
            SettingManager.highlightEnable = isChecked
        }

        // 快捷输入
        binding.swQuickInput.isChecked = SettingManager.quickInputEnable
        binding.swQuickInput.setOnCheckedChangeListener { _, isChecked ->
            SettingManager.quickInputEnable = isChecked
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}