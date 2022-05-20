package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * 选择框
 */
class ActionCheckbox(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextNewLine("- [x] ")
    }
}