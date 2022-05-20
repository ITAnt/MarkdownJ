package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * H3标题
 */
class ActionH3(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextNewLine("### ")
    }
}