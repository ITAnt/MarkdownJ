package com.itant.md.ui.share.impl.image

import android.graphics.Bitmap
import android.webkit.WebView
import com.blankj.utilcode.util.ToastUtils
import com.itant.md.R
import com.itant.md.manager.SettingManager
import com.itant.md.ui.share.ShareActivity
import com.itant.md.utils.ShareUtils
import com.itant.mvp.kt.extension.launchTask
import com.miekir.common.log.L
import com.miekir.common.widget.loading.LoadingType
import com.miekir.mvp.presenter.BasePresenter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ImagePresenter: BasePresenter<ShareImageActivity>() {
    /**
     * 保存图片
     * Write the given [Bitmap] to filesystem
     * @param targetFile The file to be written in
     * @param image      Android [Bitmap]
     * @param quality 0 - 100
     * @return True if writing was successful
     */
    private fun saveBitmapToFile(targetFile: File, bitmap: Bitmap, quality: Int = 100) {
        launchTask(
            {
                writeImageToFile(targetFile, bitmap, quality)
            }, onResult = { success, result, code, message ->
                view?.run {
                    if (result!!) {
                        ShareUtils.shareFile(this, targetFile)
                    } else {
                        ToastUtils.showShort(getString(R.string.convert_image_failed))
                    }
                    finish()
                }
            }, loadingType = LoadingType.INVISIBLE
        )
    }

    private fun writeImageToFile(targetFile: File, bitmap: Bitmap, quality: Int): Boolean {
        try {
            targetFile.parentFile?.mkdirs()
            targetFile.delete()
        } catch (e: Exception) {
            L.e(e.message)
        }
        val lc = targetFile.absolutePath.toLowerCase(Locale.ROOT)
        val format = if (lc.endsWith(".webp")) Bitmap.CompressFormat.WEBP else if (lc.endsWith(".png")) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG
        var ok = false
        val folder = File(targetFile.parent!!)
        if (folder.exists() || folder.mkdirs()) {
            var stream: FileOutputStream? = null
            try {
                stream = FileOutputStream(targetFile)
                bitmap.compress(format, quality, stream)
                ok = true
            } catch (ignored: java.lang.Exception) {
            } finally {
                try {
                    stream?.close()
                } catch (ignored: IOException) {
                }
            }
        }
        try {
            bitmap.recycle()
        } catch (ignored: java.lang.Exception) {
        }
        return targetFile.exists()
    }

    fun convertBitmap(webView: WebView) {
        launchTask(
            {
                ShareUtils.getBitmapFromWebView(webView)
            }, onResult = { success, result, code, message ->
                view?.run {
                    if (result == null) {
                        ToastUtils.showShort(getString(R.string.convert_image_failed))
                        finish()
                    } else {
                        saveBitmapToFile(File(SettingManager.exportPath, "${ShareActivity.mNoteBean!!.noteTitle}.jpg"), result)
                    }
                }
            }, loadingType = LoadingType.INVISIBLE
        )
    }
}