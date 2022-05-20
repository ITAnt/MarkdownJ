package com.itant.md.ui.share.impl.image

import android.webkit.WebView
import android.webkit.WebViewClient
import com.itant.md.ui.share.impl.html.ShareHtmlActivity
import com.itant.md.utils.ViewUtils
import com.itant.mvp.kt.extension.lazy


/**
 * 分享为图片
 */
class ShareImageActivity: ShareHtmlActivity() {

    private val imagePresenter: ImagePresenter by lazy()

    /**
     * 不要执行父类的了，否则就是分享HTML了
     */
    override fun onHtmlStringReady(result: String) {
        ViewUtils.initWebView(binding.wvDoc)
        binding.wvDoc.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // 加载完成
                imagePresenter.convertBitmap(binding.wvDoc)
            }
        }
        binding.wvDoc.loadData(result, "text/html; charset=UTF-8", null)
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewUtils.destroyWebView(binding.wvDoc)
    }

//    /**
//     * Share the given file as stream with given mime-type
//     *
//     * @param file     The file to share
//     * @param mimeType The files mime type
//     */
//    fun shareStream(file: File, mimeType: String?): Boolean {
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.putExtra("real_file_path_2", file.absolutePath)
//        intent.type = mimeType
//        try {
//            val fileUri: Uri = FileProvider.getUriForFile(this, getFileProviderAuthority(), file)
//            intent.putExtra(Intent.EXTRA_STREAM, fileUri)
//            showChooser(intent, null)
//            return true
//        } catch (ignored: java.lang.Exception) { // FileUriExposed(API24) / IllegalArgument
//        }
//        return false
//    }

//    /**
//     * 另一种保存Bitmap为图片
//     */
//    fun saveBitmapToFile(imageDirFile: File, imageName: String, b: Bitmap) {
//        if (!imageDirFile.exists()) {
//            imageDirFile.mkdirs()
//        }
//        val file = File(imageDirFile, imageName)
//
//        FileOutputStream(file).use {
//            b.compress(Bitmap.CompressFormat.JPEG, 100, it)
//            it.flush()
//            it.close()
//        }
//    }

    /*webview.setWebViewClient(new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            Picture picture = view.capturePicture();
            Bitmap b = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            picture.draw(c);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream( "/sdcard/"  + "page.jpg" );
                if ( fos != null ) {
                    b.compress(Bitmap.CompressFormat.JPEG, 90, fos );
                    fos.close();
                }
            }
            catch( Exception e ) {
                System.out.println("-----error--"+e);
            }
        }
    });

    webview.loadUrl("http://stackoverflow.com/questions/15351298/capturing-android-webview-image-and-saving-to-png-jpeg");*/
}