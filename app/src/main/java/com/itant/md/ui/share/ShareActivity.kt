package com.itant.md.ui.share

import com.itant.md.base.BaseActivity
import com.itant.md.bean.NoteBean
import com.itant.md.databinding.ActivityShareDocBinding

/**
 * @date 2022-5-16 21:20
 * @author 詹子聪
 */
open class ShareActivity: BaseActivity<ActivityShareDocBinding>() {
    companion object {
        var mNoteBean: NoteBean? = null
    }

    override fun onBindingInflate() = ActivityShareDocBinding.inflate(layoutInflater)

    override fun onInit() {

    }

    override fun onBackPressed() {}

    override fun onDestroy() {
        super.onDestroy()
        mNoteBean = null
    }
}