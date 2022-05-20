package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * 代码段
 */
class ActionCodeSnippet(private val editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextNewLine("```\n\n```")
        editText.setSelection(editText.selectionStart-4)
    }
}