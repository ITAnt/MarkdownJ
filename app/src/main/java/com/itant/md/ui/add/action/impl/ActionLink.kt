package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * 插入网址
 */
class ActionLink(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextCursorMiddle("<>")
    }
}