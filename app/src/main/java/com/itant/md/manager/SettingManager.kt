package com.itant.md.manager

import android.R.attr.centerX
import android.R.attr.centerY
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatDelegate
import com.itant.md.storage.kv.MM
import com.miekir.common.context.GlobalContext
import java.io.File
import java.util.*
import kotlin.math.hypot


object SettingManager {
    /**
     * 当前主题模式
     */
    var themeMode by MM("theme", AppCompatDelegate.MODE_NIGHT_NO)

    /**
     * 文档导出路径
     */
    var exportPath by MM("path_export", File(GlobalContext.getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Notes").absolutePath)

    /**
     * 是否开启实时预览
     */
    var livePreviewEnable by MM("live_preview", false)

    /**
     * 语法高亮
     */
    var highlightEnable by MM("highlight", true)

    /**
     * 快捷输入
     */
    var quickInputEnable by MM("quick_input", true)

    /**
     * 应用主题
     */
    fun applyThemeMode(mode: Int = AppCompatDelegate.MODE_NIGHT_NO) {
        if (themeMode == mode) {
            return
        }
        themeMode = if (mode != AppCompatDelegate.MODE_NIGHT_NO && mode != AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            mode
        }
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    /**
     * 是否为暗黑模式
     */
    fun isNightMode(): Boolean {
        return themeMode == AppCompatDelegate.MODE_NIGHT_YES
    }

    /**
     * 切换动画
     */
    fun animateTheme(view: View) {
        val animator = ViewAnimationUtils.createCircularReveal(
            view,
            centerX,
            centerY, 0f, hypot(view.width.toDouble(), view.height.toDouble()).toFloat()
        )
        animator.interpolator = AccelerateInterpolator()
        animator.duration = 500
        animator.start()
    }

    /**
     * 判断当前是否简体中文环境
     */
    fun isSimpleChinese(): Boolean {
        return TextUtils.equals(Locale.getDefault().country, "CN")
    }
}