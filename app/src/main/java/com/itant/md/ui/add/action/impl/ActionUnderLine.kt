package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * 下划线
 */
class ActionUnderLine(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextCursorOffset("<u></u>", 3)
    }
}