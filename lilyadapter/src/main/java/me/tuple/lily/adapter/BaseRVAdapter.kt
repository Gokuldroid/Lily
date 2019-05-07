package me.tuple.lily.adapter

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import me.tuple.lily.core.findById
import me.tuple.lily.core.unless

/**
 * Created by LazyLoop.
 */
abstract class BaseRVAdapter<T>(val dataHolder: DataHolder<T, out RVHolder<T>>) : androidx.recyclerview.widget.RecyclerView.Adapter<RVHolder<T>>() {
    var isDisabled: Boolean = false
    var onClickListener: OnClickListener<T>? = null
    var onLongClickListener: OnLongClickListener<T>? = null
    var multiSelectListener: MultiSelectListener<T>? = null

    fun enable() {
        isDisabled = false
    }

    fun disable() {
        isDisabled = true
    }

    override fun onBindViewHolder(holder: RVHolder<T>, position: Int) {
        holder.bind(getItem(position))
        onClickListener?.apply {
            holder.itemView.setOnClickListener {
                unless(isDisabled) {
                    if (dataHolder.isEmptySelection) {
                        val adapterPosition = holder.adapterPosition
                        if (adapterPosition == androidx.recyclerview.widget.RecyclerView.NO_POSITION) {
                            return@setOnClickListener
                        }
                        if (this.onClick(getItem(adapterPosition), adapterPosition)) {
                            notifyItemChanged(adapterPosition)
                        }
                    } else {
                        holder.itemView.performLongClick()
                    }
                }
            }
        }

        onLongClickListener?.apply {
            holder.itemView.setOnLongClickListener {
                unless(isDisabled) {
                    val adapterPosition = holder.adapterPosition
                    if (adapterPosition == androidx.recyclerview.widget.RecyclerView.NO_POSITION) {
                        return@setOnLongClickListener true
                    }
                    this.onLongClick(getItem(adapterPosition), adapterPosition)
                    dataHolder.toggleSelection(adapterPosition)
                    multiSelectListener?.onChange(dataHolder)
                }
                return@setOnLongClickListener true
            }
        }
    }

    fun getItem(position: Int): T = dataHolder.get(position)

    fun setRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView): BaseRVAdapter<T> {
        recyclerView.adapter = this
        dataHolder.adapter = this
        return this
    }

    override fun getItemCount(): Int = dataHolder.size()
}

abstract class RVHolder<in T>(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    fun <V : View> findById(@IdRes id: Int): V = itemView.findById(id)
    abstract fun bind(data: T)
}

interface OnClickListener<T> {
    fun onClick(data: T, position: Int): Boolean
}

interface OnLongClickListener<T> {
    fun onLongClick(data: T, position: Int): Boolean
}

interface MultiSelectListener<T> {
    fun onChange(dataHolder: DataHolder<T, out RVHolder<T>>)
}