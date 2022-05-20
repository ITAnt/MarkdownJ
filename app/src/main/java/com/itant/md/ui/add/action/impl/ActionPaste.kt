package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.blankj.utilcode.util.ClipboardUtils
import com.itant.md.ui.add.action.AbstractAction

/**
 * 粘贴
 */
class ActionPaste(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertText(ClipboardUtils.getText().toString())
    }
}