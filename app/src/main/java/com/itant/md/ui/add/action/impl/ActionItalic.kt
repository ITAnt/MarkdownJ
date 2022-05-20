package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * 倾斜
 */
class ActionItalic(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextCursorMiddle("**")
    }
}