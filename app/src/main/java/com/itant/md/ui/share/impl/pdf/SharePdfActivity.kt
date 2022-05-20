package com.itant.md.ui.share.impl.pdf

import android.print.*
import android.print.PrintAttributes.Margins
import android.print.PrintAttributes.Resolution
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.blankj.utilcode.util.ToastUtils
import com.itant.md.R
import com.itant.md.databinding.ActivityShareDocBinding
import com.itant.md.manager.SettingManager
import com.itant.md.ui.share.ShareActivity
import com.itant.md.ui.share.impl.html.ShareHtmlActivity
import com.itant.md.utils.ViewUtils
import java.io.File


/**
 * 分享为PDF文件
 */
class SharePdfActivity: ShareHtmlActivity() {

    /**
     * 打印是否已启动
     */
    private var printStarted = false

    override fun onBindingInflate() = ActivityShareDocBinding.inflate(layoutInflater)

    /**
     * 不要执行父类的了，否则就是分享HTML了
     */
    override fun onHtmlStringReady(result: String) {
        ViewUtils.initWebView(binding.wvDoc)
        binding.wvDoc.webViewClient = WebViewClient()
        binding.wvDoc.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    //加载完网页进度条消失
                    val job = shareAsPdf(binding.wvDoc, "${ShareActivity.mNoteBean!!.noteTitle}.pdf")
                    if (job == null) {
                        ToastUtils.showShort(getString(R.string.pdf_convert_failed))
                        finish()
                    }
                }
            }
        }
        binding.wvDoc.loadData(result, "text/html; charset=UTF-8", null)
    }

    override fun onStop() {
        super.onStop()
        printStarted = true
    }

    override fun onResume() {
        super.onResume()
        if (printStarted) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewUtils.destroyWebView(binding.wvDoc)
    }

    private fun generatePdfFile(title: String) {
        val jobName = "Pdf Document"
        val attributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setResolution(Resolution("pdf", "pdf", 600, 600))
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build()
        val pdfPrint = PdfPrint(attributes)
        pdfPrint.print(
            binding.wvDoc.createPrintDocumentAdapter(jobName),
            File(SettingManager.exportPath),
            "${title}.pdf"
        )
    }

    /**
     * 分享为PDF文件（打印，可以另存为），需要等待渲染结束后再调用（所以可能要等待WebView加载完成后调用）
     */
    fun shareAsPdf(webView: WebView, jobName: String, landscape: Boolean = false): PrintJob? {
        val printManager = getSystemService(PRINT_SERVICE) as PrintManager
        val printAdapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter(jobName)
        val attrib = PrintAttributes.Builder()
        if (landscape) {
            attrib.setMediaSize(PrintAttributes.MediaSize("ISO_A4", "android", 11690, 8270))
            attrib.setMinMargins(Margins(0, 0, 0, 0))
        }
        try {
            return printManager.print(jobName, printAdapter, attrib.build())
        } catch (ignored: Exception) { }
        return null
    }
}