package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * 无序列表
 */
class ActionListPoint(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextNewLine("- ")
    }
}