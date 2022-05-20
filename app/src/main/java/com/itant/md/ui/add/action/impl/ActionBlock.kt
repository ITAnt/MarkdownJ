package com.itant.md.ui.add.action.impl

import android.widget.EditText
import com.itant.md.ui.add.action.AbstractAction

/**
 * 区块引用
 */
class ActionBlock(editText: EditText): AbstractAction(editText) {
    override fun execute() {
        insertTextNewLine("> ")
    }
}