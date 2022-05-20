package com.itant.md.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.util.*

/**
 * adapter拖拽回调
 */
class TouchCallback<T, VH : BaseViewHolder>(val adapter: EditableAdapter<T, VH>) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        if (!adapter.editEnable) {
            return 0
        }

        var dragFlag = 0
        if (recyclerView.layoutManager is GridLayoutManager) {
            dragFlag = ItemTouchHelper.UP or
                    ItemTouchHelper.DOWN or
                    ItemTouchHelper.LEFT or
                    ItemTouchHelper.RIGHT
        } else if (recyclerView.layoutManager is LinearLayoutManager) {
            dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        }
        return makeMovementFlags(dragFlag, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        //得到当前拖拽的viewHolder的Position
        val fromPosition = viewHolder.absoluteAdapterPosition
        //拿到当前拖拽到的item的viewHolder
        val toPosition = target.absoluteAdapterPosition
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(adapter.dataList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(adapter.dataList, i, i - 1)
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //侧滑删除可以使用
    }

    override fun isLongPressDragEnabled(): Boolean {
        // 该方法默认返回true就是不屏蔽拖动效果，所有的Item都能被拖动。如果要实现屏蔽的话，这里我们只要返回false就可以了。
        return true
    }

    /**
     * 长按选中Item的时候开始调用
     * 长按高亮
     * @param viewHolder
     * @param actionState
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // 通过重写该方法我们可以定义自己的拖动背景
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {

        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * 手指松开的时候还原高亮
     * @param recyclerView
     * @param viewHolder
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        // 完成拖动后刷新适配器，这样拖动后删除就不会错乱
        adapter.notifyDataSetChanged()
    }

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return super.canDropOver(recyclerView, current, target)
    }
}
