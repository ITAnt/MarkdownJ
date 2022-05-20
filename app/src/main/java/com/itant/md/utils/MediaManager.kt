package com.itant.md.utils

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.text.TextUtils
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.miekir.common.context.GlobalContext
import okhttp3.MediaType
import java.io.File
import java.util.*


/**
 * 图片/视频选择器（选择一张/个）
 * @date 2021-10-23 11:27
 * @author 詹子聪
 */
object MediaManager {

    /**
     * 是否是视频链接
     */
    fun isVideo(url: String?): Boolean {
        if (TextUtils.isEmpty(url) || !url!!.contains(""".""")) {
            return false
        }
        val index = url.lastIndexOf(""".""")
        val suffix = url.substring(index + 1).toLowerCase(Locale.getDefault())
        return "wmv,avi,dat,asf,mpeg,mpg,rm,rmvb,ram,flv,mp4,3gp,mov,divx,dv,vob,mkv,qt,cpk,fli,flc,f4v,m4v,mod,m2t,swf,webm,mts,m2ts".contains(
            suffix)
    }

    /**
     * 根据文件Uri获取mimeType
     */
    fun getMimeType(uri: Uri): String? {
        var mimeType: String? = null
        mimeType = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val cr: ContentResolver = GlobalContext.getContext().contentResolver
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(fileExtension.toLowerCase(Locale.getDefault()))
        }
        return mimeType
    }

    /**
     * 获取媒体类型
     */
    fun getMediaType(file: File): MediaType? {
        val name = FileUtils.getFileName(file)
        if (!name.contains(""".""")) {
            return null
        }
        val index = name.lastIndexOf(""".""")
        if (index == name.length - 1) {
            return null
        }

        val mimeType = getMimeType(file.toUri()) ?: return null
        try {
            return MediaType.parse(mimeType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 打开文件夹
     */
    fun openFolder(path: String) {
//        val downloadIntent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
//        downloadIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        ActivityUtils.getTopActivity().startActivity(downloadIntent)

//        val intent = Intent(Intent.ACTION_VIEW)
//        val uri = FileProvider.getUriForFile(GlobalContext.getContext(), "${GlobalContext.getContext().packageName}.provider", File(path))
//        intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR)
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//        ActivityUtils.getTopActivity().startActivity(intent);

        val file = File(path)
        if (!file.exists()) {
            ToastUtils.showShort("文件夹不存在")
            return
        }

        val uri = FileProvider.getUriForFile(GlobalContext.getContext(),
            "${GlobalContext.getContext().packageName}.provider",
            File(path))
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_VIEW
            putExtra(Intent.EXTRA_STREAM, uri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            type = "*/*"
            //type = MediaManager.getMimeType(uri)
        }
        shareIntent.addCategory(Intent.CATEGORY_DEFAULT)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (shareIntent.resolveActivityInfo(GlobalContext.getContext().packageManager, 0) != null) {
            GlobalContext.getContext().startActivity(shareIntent)
            return
        }

        try {
            GlobalContext.getContext().startActivity(Intent.createChooser(shareIntent, "选择浏览工具"))
        } catch (exception: Exception) {
            ToastUtils.showShort("找不到浏览工具")
            exception.printStackTrace()
        }
    }

    /**
     * 打开下载目录
     */
    fun openDownloadFolder() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 初始化打开下载目录
                val uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:Download/")
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
            }
            // 提供访问子目录的权限
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        ActivityUtils.getTopActivity().startActivity(intent)
    }
}