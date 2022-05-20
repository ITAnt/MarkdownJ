package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * 分割线
 */
class ActionSplit(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextNewLine("***")
    }
}