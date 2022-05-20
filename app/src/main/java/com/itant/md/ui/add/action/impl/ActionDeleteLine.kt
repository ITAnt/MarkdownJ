package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * 删除线
 */
class ActionDeleteLine(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextCursorMiddle("~~~~")
    }
}