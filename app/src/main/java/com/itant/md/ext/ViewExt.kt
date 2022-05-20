package com.itant.md.ext

import android.app.Dialog
import android.text.TextUtils
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.itant.md.base.BaseActivity
import com.itant.md.base.BaseActivity.Companion.SIZE_IN_DP_WIDTH
import com.itant.md.ui.share.AbstractShare
import com.itant.mvp.kt.extension.requestPermissions
import com.miekir.common.autosize.AutoSizeCompat
import com.miekir.common.tools.ToastTools
import io.noties.markwon.Markwon
import io.noties.markwon.PrecomputedTextSetterCompat
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import io.noties.markwon.simple.ext.SimpleExtPlugin
import java.util.concurrent.Executors

/**
 * 获取文本
 */
fun TextView.getString(message: String? = null): String? {
    val text = this.text.toString()
    if (TextUtils.isEmpty(text)) {
        if (!TextUtils.isEmpty(message)) {
            ToastTools.showShort(message)
        }
        return null
    }
    return text
}

/**
 * 适配对话框
 */
fun Dialog.showAdapt() {
    val width = if (ScreenUtils.getAppScreenWidth() < ScreenUtils.getAppScreenHeight()) {
        SIZE_IN_DP_WIDTH
    } else {
        SIZE_IN_DP_WIDTH * ScreenUtils.getAppScreenWidth() * 1.0f / ScreenUtils.getAppScreenHeight()
    }
    AutoSizeCompat.autoConvertDensity(context.resources, width, ScreenUtils.getAppScreenWidth() < ScreenUtils.getAppScreenHeight())
    show()
}

/**
 * 执行分享，执行之前确保相关权限通过了
 */
fun BaseActivity<out ViewBinding>.performShare(vararg permissions: String, action: AbstractShare) {
    requestPermissions(*permissions, callback = { granted, temp ->
        if (granted) {
            action.run()
        } else {
            ToastUtils.showShort("请先授予权限")
        }
    })
}

/**
 * 初始化新建markdown
 */
fun TextView.newMarkwon(): Markwon.Builder {
    return Markwon.builder(context)
        .usePlugin(TaskListPlugin.create(context))         // 有序无序列表
        .usePlugin(TablePlugin.create(context))            // 表格
        .usePlugin(HtmlPlugin.create())                 // html
        .usePlugin(StrikethroughPlugin.create())        // 删除线
        .usePlugin(GlideImagesPlugin.create(context))      // 图片
        .usePlugin(MarkwonInlineParserPlugin.create())  // 公式
        .usePlugin(JLatexMathPlugin.create(textSize) { builder ->
            builder.inlinesEnabled(true)
        })
        .usePlugin(LinkifyPlugin.create())              // 链接
        .usePlugin(SimpleExtPlugin.create())
        .textSetter(PrecomputedTextSetterCompat.create(Executors.newCachedThreadPool())) // 文字如何应用到TextView
}