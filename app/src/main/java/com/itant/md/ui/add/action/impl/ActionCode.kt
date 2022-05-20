package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * 代码
 */
class ActionCode(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextCursorMiddle("``")
    }
}