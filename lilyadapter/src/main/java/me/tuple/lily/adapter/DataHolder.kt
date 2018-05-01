package me.tuple.lily.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by LazyLoop.
 */
abstract class DataHolder<T, VH : RecyclerView.ViewHolder>() {
    lateinit var adapter: BaseRVAdapter<T>
    var selectedCount: Int = 0
    var selectedItems: SparseBooleanArray = SparseBooleanArray();
    abstract fun get(position: Int): T
    abstract fun size(): Int
    abstract fun isEmpty(): Boolean
    abstract fun clear()
    abstract fun add(item: T)
    abstract fun removeItemAt(position: Int)
    abstract fun removeItem(item: T)

    fun isSelectable(position: Int): Boolean = true

    var isEmptySelection: Boolean = true
        get() = selectedCount == 0

    fun reset() {
        selectedCount = 0
        selectedItems = SparseBooleanArray()
        adapter.notifyDataSetChanged()
    }

    fun select(position: Int): Boolean {
        if (isSelectable(position) && !selectedItems.get(position)) {
            selectedItems.put(position, true)
            selectedCount++
            adapter.notifyItemChanged(position)
            return true
        }
        return false
    }

    fun deSelect(position: Int): Boolean {
        if (isSelectable(position) && selectedItems.get(position)) {
            selectedItems.put(position, false)
            selectedCount--
            adapter.notifyItemChanged(position)
            return true
        }
        return false
    }

    fun selectAll() {
        performBulkSelection(true)
    }

    fun deselectAll() {
        performBulkSelection(false)
    }

    private fun performBulkSelection(isSelected: Boolean) {

        val oldSelectedCount = selectedCount
        var nonSelectableItems = 0
        for (i in 0 until adapter.itemCount) {
            if (isSelectable(i)) {
                selectedItems.put(i, isSelected)
            } else {
                nonSelectableItems++
            }
        }
        selectedCount = if (isSelected) {
            adapter.itemCount - nonSelectableItems
        } else {
            0
        }
        if (oldSelectedCount != selectedCount) {
            adapter.notifyDataSetChanged()
        }
    }


    fun isSelected(position: Int): Boolean = selectedItems.get(position)

    fun toggleSelection(position: Int) {
        if (isSelected(position)) {
            deSelect(position)
        } else {
            select(position)
        }
    }

    fun getSelectedItems(): MutableList<T> {
        if (selectedCount == 0) {
            return Collections.emptyList<T>()
        }
        val result = ArrayList<T>(selectedCount)
        for (i in 0 until size()) {
            if (isSelected(i)) {
                result.add(get(i))
            }
        }
        return result
    }
}