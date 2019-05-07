package me.tuple.lily.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by LazyLoop.
 */
open class ListDataHolder<T, VH : androidx.recyclerview.widget.RecyclerView.ViewHolder>(private var data: MutableList<T>) : DataHolder<T, VH>() {

    override fun get(position: Int): T = data[position]

    override fun size(): Int = data.size

    override fun isEmpty(): Boolean = data.isEmpty()

    fun reset(data: MutableList<T>) {
        this.data = data
        reset()
    }

    override fun clear() {
        data.clear()
        reset()
    }

    override fun add(item: T) {
        data.add(item)
        adapter.notifyItemInserted(data.size)
    }

    override fun removeItemAt(position: Int) {
        data.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    override fun removeItem(item: T) {
        val index = data.indexOf(item)
        if (index != -1) {
            removeItemAt(index)
        }
    }
}