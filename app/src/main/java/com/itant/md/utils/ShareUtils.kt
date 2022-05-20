package com.itant.md.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.webkit.WebView
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import com.itant.md.R
import com.miekir.common.log.L
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * @date 2022-5-16 22:09
 * @author 詹子聪
 */
object ShareUtils {
    /**
     * 分享文件
     */
    fun shareFile(context: Context, file: File) {
        val fileUri = UriUtils.file2Uri(file)
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, fileUri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            //type = "*/*"
            type = MediaManager.getMimeType(fileUri)
        }
        try {
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_file)))
        } catch (e: Exception) {
            ToastUtils.showShort(e.message)
        }
    }

    /**
     * 把字符串写到文件
     */
    suspend fun writeStringToFile(targetFile: File, content: String): Boolean {
        withContext(Dispatchers.IO) {
            try {
                targetFile.parentFile?.mkdirs()
                targetFile.delete()
                targetFile.bufferedWriter().use { out -> out.write(content) }
            } catch (e: Exception) {
                L.e(e.message)
            }
            //targetFile.writeText(content)
        }
        return targetFile.exists()
    }

    /**
     * 读文件字符串
     */
    suspend fun readStringFromFile(targetFile: File): String {
        var result = ""
        if (!targetFile.exists()) {
            return result
        }
        result = withContext(Dispatchers.IO) {
            InputStreamReader(FileInputStream(targetFile)).use {
                return@withContext it.readText()
            }
        }
        return result
    }

    /**
     * WebView加载转图片
     */
    suspend fun getBitmapFromWebView(webView: WebView, fullPage: Boolean = true): Bitmap? {
        return try {
            //Measure WebView's content
            if (fullPage) {
                val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                webView.measure(widthMeasureSpec, heightMeasureSpec)
                webView.layout(0, 0, webView.measuredWidth, webView.measuredHeight)
            }

            //Build drawing cache and store its size
            webView.buildDrawingCache()

            //Creates the bitmap and draw WebView's content on in
            val bitmap = Bitmap.createBitmap(
                webView.measuredWidth,
                webView.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(bitmap, 0f, bitmap.height.toFloat(), Paint())
            webView.draw(canvas)
            webView.destroyDrawingCache()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            null
        }
    }
}