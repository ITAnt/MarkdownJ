package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * 插入图片链接
 */
class ActionPhoto(val editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextNewLine("![image]()")
        editText.setSelection(editText.selectionStart-1)
    }
}