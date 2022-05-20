package com.itant.md.ui.add.action

import android.widget.EditText


/**
 * 点击快捷图标后添加相应的markdown语法
 */
abstract class AbstractAction(private val editText: EditText) {
    /**
     * 执行
     */
    abstract fun execute()

    /**
     * 在当前光标处插入文本
     */
    protected fun insertText(text: String) {
        editText.text.insert(editText.selectionStart, text)
    }

    /**
     * 在当前光标处插入文本，并且将光标移动到插入的文本中间
     */
    protected fun insertTextCursorMiddle(text: String) {
        val end = editText.selectionStart + (text.length/2)
        insertText(text)
        editText.setSelection(end)
    }

    /**
     * 在当前光标处插入文本，并且将光标移动到插入的文本偏移指定位置
     * @param offset 偏移的位置
     */
    protected fun insertTextCursorOffset(text: String, offset: Int) {
        val end = editText.selectionStart + offset
        insertText(text)
        editText.setSelection(end)
    }

    /**
     * 在当前光标处插入文本，并且选中插入的文本
     */
    protected fun insertTextAndSelect(text: String) {
        val start = editText.selectionStart
        val end = start + text.length
        insertText(text)
        editText.setSelection(start, end)
    }

    /**
     * \n算一行
     * 0A1B2：selectionStart为数字，字母为内容
     * 在当前光标处插入文本，并且独立成行
     */
    protected fun insertTextNewLine(text: String) {
        val lineStart = editText.getCurrentLineStart()
        if (editText.selectionStart == lineStart) {
            // 在行的开头插入
            beforeInsert()
            insertText(text)
            afterInsert()
        } else {
            // 不是在行的开头插入
            // 先移动光标到当前行的末尾
            val lineEnd = editText.getCurrentLineEnd()
            editText.setSelection(lineEnd)
            // 判断前面要不要插入换行
            if (editText.text.toString()[editText.selectionStart-1] != '\n') {
                insertText("\n\n")
            } else {
                insertText("\n")
            }
            insertText(text)

            afterInsert()
        }
    }

    /**
     * 判断后面是不是需要换行
     */
    private fun afterInsert() {
        // 插入后，判断后面是不是需要换行
        if (editText.selectionStart < editText.text.toString().length) {
            if (editText.text.toString()[editText.selectionStart] != '\n') {
                insertText("\n\n")
                editText.setSelection(editText.selectionStart-2)
            } else {
                if (editText.selectionStart+1 < editText.text.toString().length) {
                    if (editText.text.toString()[editText.selectionStart+1] != '\n') {
                        insertText("\n")
                        editText.setSelection(editText.selectionStart-1)
                    }
                } else {
                    insertText("\n")
                    editText.setSelection(editText.selectionStart-1)
                }
            }
        } else {
            insertText("\n\n")
            editText.setSelection(editText.selectionStart-2)
        }
    }

    /**
     * 插入前，判断前面是不是需要换行
     */
    private fun beforeInsert() {
        if (editText.selectionStart > 1) {
            if (editText.text.toString()[editText.selectionStart-1] != '\n') {
                insertText("\n")
            } else {
                if (editText.text.toString()[editText.selectionStart-2] != '\n') {
                    insertText("\n")
                }
            }
        }
    }
}