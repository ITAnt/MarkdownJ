package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * H2标题
 */
class ActionH2(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextNewLine("## ")
    }
}