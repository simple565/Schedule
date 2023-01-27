package com.maureen.schedule.utils

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.maureen.schedule.R
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Description:
 * @author Maureen
 * Create 2023-01-24 21:16
 */
class ItemSwipeHelper : ItemTouchHelper.Callback() {
    companion object{
        private const val TAG = "ItemSwipeHelper"
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // 设置是否需要上下拖动或左右滑动
        return makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (viewHolder.itemViewType != target.itemViewType) {
            return false
        }
        // 在拖动过程中每次两个相邻 Item 之间交换位置都会调用
        val fromPosition = viewHolder.adapterPosition
        val targetPosition = target.adapterPosition
        Log.d(TAG, "onMove: from $fromPosition target $targetPosition")
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 侧滑时调用
        Log.d(TAG, "onSwiped: ${viewHolder.adapterPosition}")
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        Log.d(TAG, "onSelectedChanged: ${viewHolder?.adapterPosition} $actionState")
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        Log.d(TAG, "onChildDraw: ${viewHolder.adapterPosition}  $actionState")
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            Log.d(TAG, "onChildDraw")
            val deleteBackground = ContextCompat.getDrawable(recyclerView.context, R.color.red_500)
            val editBackground = ContextCompat.getDrawable(recyclerView.context, R.color.orange_500)
            val deleteIcon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_delete)
            val editIcon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_edit)

            val itemView = viewHolder.itemView
            c.save()
            if (dX > 0) {
                c.clipRect(itemView.left.toFloat(), itemView.top.toFloat(), itemView.left + dX, itemView.bottom.toFloat())
                c.drawColor(ContextCompat.getColor(recyclerView.context, R.color.orange_500))
            } else {
                c.clipRect(itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                c.drawColor(ContextCompat.getColor(recyclerView.context, R.color.red_500))
            }
            itemView.translationX = dX
            c.restore()
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        Log.d(TAG, "clearView: ${viewHolder.adapterPosition} ${viewHolder.itemView.scrollX}")
        super.clearView(recyclerView, viewHolder)
    }
}
