package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * H1标题
 */
class ActionH1(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextNewLine("# ")
    }
}