package com.itant.md.ui.add.action

import android.app.Activity
import android.text.Selection
import android.view.View
import android.widget.EditText
import com.itant.md.databinding.ActivityAddBinding
import com.itant.md.ui.add.action.impl.*

/**
 * 绑定图标id和对应的执行动作
 */
fun ActivityAddBinding.bindAction(activity: Activity) {
    val actionMap = hashMapOf<Int, AbstractAction>()
    val clickListener = View.OnClickListener { v -> actionMap[v?.id]?.execute() }
    actionMap[ivEditPaste.id] = ActionPaste(etContent)
    actionMap[ivEditH1.id] = ActionH1(etContent)
    actionMap[ivEditH2.id] = ActionH2(etContent)
    actionMap[ivEditH3.id] = ActionH3(etContent)
    actionMap[ivEditListPoint.id] = ActionListPoint(etContent)
    actionMap[ivEditBlock.id] = ActionBlock(etContent)
    actionMap[ivEditCode.id] = ActionCode(etContent)
    actionMap[ivEditCodeSnippet.id] = ActionCodeSnippet(etContent)
    actionMap[ivEditBold.id] = ActionBold(etContent)
    actionMap[ivEditItalic.id] = ActionItalic(etContent)
    actionMap[ivEditSplit.id] = ActionSplit(etContent)
    actionMap[ivEditCheckbox.id] = ActionCheckbox(etContent)
    actionMap[ivEditDeleteLine.id] = ActionDeleteLine(etContent)
    actionMap[ivEditUnderline.id] = ActionUnderLine(etContent)
    actionMap[ivEditPhoto.id] = ActionPhoto(etContent)
    actionMap[ivEditLink.id] = ActionLink(etContent)
    actionMap[ivEditTable.id] = ActionTable(etContent)
    actionMap[ivEditTime.id] = ActionTime(etContent)
    // 监听点击事件
    for ((key, _) in actionMap) {
        activity.findViewById<View>(key).setOnClickListener(clickListener)
    }
}

/**
 * 获取当前光标在第几行
 */
fun EditText.getCurrentCursorLineNumber(): Int {
    val selectionStart = Selection.getSelectionStart(text)
    return if (selectionStart != -1) {
        layout.getLineForOffset(selectionStart)
    } else -1
}

/**
 * 返回当前行开始的位置
 */
fun EditText.getCurrentLineStart(): Int {
    return layout.getLineStart(getCurrentCursorLineNumber())
}

/**
 * 返回当前行结束的位置
 */
fun EditText.getCurrentLineEnd(): Int {
    return layout.getLineEnd(getCurrentCursorLineNumber())
}

/**
 * 返回当前行文字
 */
fun EditText.getCurrentLineString(): CharSequence {
    val start = getCurrentLineStart()
    val end = getCurrentLineEnd()
    return if (end > start) {
        text.toString().subSequence(start, end)
    } else {
        ""
    }
}

