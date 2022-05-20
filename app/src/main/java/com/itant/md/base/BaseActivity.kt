package com.itant.md.base

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.forEach
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ScreenUtils
import com.itant.mvp.kt.extension.enableHighRefreshRate
import com.miekir.mvp.view.binding.adapt.BindingActivity

/**
 * @date 2022-1-16 20:52
 * @author 詹子聪
 */
abstract class BaseActivity<VB : ViewBinding> : BindingActivity<VB>() {
    private var mMenu: Menu? = null

    /**
     * 是否为第一次加载
     */
    protected var mIsFirstLoad = true

    companion object {
        const val SIZE_IN_DP_WIDTH = 375.0f
    }

    override fun isBaseOnWidth(): Boolean {
        return ScreenUtils.getAppScreenWidth() < ScreenUtils.getAppScreenHeight()
    }

    override fun getSizeInDp(): Float {
        return SIZE_IN_DP_WIDTH
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        touchSpaceHideKeyboard = true
        // 启用高刷新率
        enableHighRefreshRate()
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        // 必须要在onPause隐藏键盘，在onDestroy就太晚了
        KeyboardUtils.hideSoftInput(this)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        mMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && mIsFirstLoad) {
            mIsFirstLoad = false
            mMenu?.forEach {
                val view = findViewById<View>(it.itemId)
                if (view != null) {
                    TooltipCompat.setTooltipText(view, null)
                }
            }
        }
    }
}