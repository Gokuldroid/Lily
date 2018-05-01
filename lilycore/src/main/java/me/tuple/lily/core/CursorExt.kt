package me.tuple.lily.core

import android.database.Cursor
import android.support.v4.util.ArrayMap
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by LazyLoop.
 */

open class CursorWrapper(val cursor: Cursor?){

    private val columnIndexMap: ArrayMap<String, Int> = ArrayMap()

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

    fun getBlob(column: String): ByteArray? {
        return this.cursor!!.getBlob(columnIndexMap[column]!!)
    }

    fun getString(column: String, defaultValue: String = ""): String {
        return this.cursor!!.getString(columnIndexMap[column]!!) ?: defaultValue
    }

    fun getShort(column: String): Short {
        return this.cursor!!.getShort(columnIndexMap[column]!!)
    }

    fun getInt(column: String): Int {
        return this.cursor!!.getInt(columnIndexMap[column]!!)
    }

    fun getLong(column: String): Long {
        return this.cursor!!.getLong(columnIndexMap[column]!!)
    }

    fun getFloat(column: String): Float {
        return this.cursor!!.getFloat(columnIndexMap[column]!!)
    }

    fun getDouble(column: String): Double {
        return this.cursor!!.getDouble(columnIndexMap[column]!!)
    }

    fun isNull(column: String): Boolean {
        return this.cursor!!.isNull(columnIndexMap[column]!!)
    }

    companion object {
        fun from(cursor: Cursor?): CursorWrapper {
            return CursorWrapper(cursor)
        }
    }
}