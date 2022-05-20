package com.itant.md.adapter

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 可以编辑的适配器
 * @date 2021-9-4 23:28
 * @author 詹子聪
 */
abstract class EditableAdapter<T, VH : BaseViewHolder>
@JvmOverloads constructor(@LayoutRes private val layoutResId: Int, var dataList: MutableList<T>) :
    BaseQuickAdapter<T, VH>(layoutResId, dataList) {
    /**
     * 是否可以拖拽编辑
     */
    var editEnable = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * 解决EditText在RecyclerView中乱序的问题
     */
    protected fun handleEdit(editText: EditText, initValue: String?, textWatcher: ((value: String) -> Unit)? = null) {
        var watcher = editText.getTag(editText.id) as TextWatcher?
        watcher?.run {
            editText.removeTextChangedListener(this)
        }
        editText.setText(initValue)
        watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                textWatcher?.invoke(s.toString())
            }
        }
        editText.addTextChangedListener(watcher)
        editText.setTag(editText.id, watcher)
    }
}