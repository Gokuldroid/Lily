package me.tuple.lily.core

import android.database.Cursor
import androidx.collection.ArrayMap
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by LazyLoop.
 */

open class CursorWrapper(val cursor: Cursor?){

    private val columnIndexMap: androidx.collection.ArrayMap<String, Int> = androidx.collection.ArrayMap()

    init {
        cursor?.apply {
            columnNames.forEach {
                columnIndexMap[it] = getColumnIndexOrThrow(it)
            }
        }
    }


    inline fun forEach(consumer: (CursorWrapper) -> Unit): Boolean {
        return safeExecute {
            cursor?.use {
                while (it.moveToNext()) {
                    consumer.invoke(this)
                }
            }
        }
    }

    inline fun <T> toList(transformer: (CursorWrapper) -> T): ArrayList<T> {
        cursor?.also {
            val result = ArrayList<T>(it.count)
            forEach { result.add(transformer.invoke(it)) }
            return result
        }
        return Collections.emptyList<T>() as ArrayList<T>
    }

    fun getBlob(column: String): ByteArray? = this.cursor!!.getBlob(columnIndexMap[column]!!)

    fun getString(column: String, defaultValue: String = ""): String =
            this.cursor!!.getString(columnIndexMap[column]!!) ?: defaultValue

    fun getShort(column: String): Short = this.cursor!!.getShort(columnIndexMap[column]!!)

    fun getInt(column: String): Int = this.cursor!!.getInt(columnIndexMap[column]!!)

    fun getLong(column: String): Long = this.cursor!!.getLong(columnIndexMap[column]!!)

    fun getFloat(column: String): Float = this.cursor!!.getFloat(columnIndexMap[column]!!)

    fun getDouble(column: String): Double = this.cursor!!.getDouble(columnIndexMap[column]!!)

    fun isNull(column: String): Boolean = this.cursor!!.isNull(columnIndexMap[column]!!)

    companion object {
        fun from(cursor: Cursor?): CursorWrapper = CursorWrapper(cursor)
    }
}