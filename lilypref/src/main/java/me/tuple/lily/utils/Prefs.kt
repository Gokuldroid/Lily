package me.tuple.lily.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import me.tuple.lily.core.Contexter
import java.util.*

/**
 * Created by gokul on 06/04/2016.
 * This class helps to store and retrieve data form shared preferences.
 */


inline fun SharedPreferences.use(function: SharedPreferences.Editor.() -> Unit): Boolean {
    val editor = this.edit()
    function.invoke(editor)
    return editor.commit()
}

@SuppressLint("CommitPrefEdits")
@Suppress("Unused")
class Prefs(context: Context, name: String) {

    constructor(function: Prefs.() -> Unit) : this(Contexter.context, defaultPrefName) {
        function(this)
    }

    val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun set(key: String, value: Any?): Boolean {
        return if (value == null)
            remove(key)
        else preferences.use {
            when (value) {
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Set<*> -> {
                    putStringSet(key, value as MutableSet<String>?)
                }
                else -> throw IllegalArgumentException("Unknown Type for preference")
            }
        }
    }

    fun setAndGet(key: String, value: Any?): Any? {
        set(key, value)
        return value
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long = 0): Long {
        return preferences.getLong(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return preferences.getFloat(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return preferences.getString(key, defaultValue)
    }

    fun getStringSet(key: String, defaultValue: HashSet<String>? = null): Set<String>? {
        return preferences.getStringSet(key, defaultValue)
    }

    val map: Map<String, *>
        get() = preferences.all

    fun remove(key: String): Boolean {
        return preferences.use {
            remove(key)
        }
    }

    fun clear(): Boolean {
        return preferences.use {
            clear()
        }
    }

    operator fun contains(key: String): Boolean {
        return preferences.contains(key)
    }

    companion object {
        private val defaultPreference: Prefs by lazy {
            Prefs(Contexter.context, defaultPrefName)
        }

        private val defaultPrefName: String by lazy {
            PreferenceManager.getDefaultSharedPreferencesName(Contexter.context)
        }

        fun instance(name: String?): Prefs {
            return if (name == null) defaultPreference else Prefs(Contexter.context, name)
        }

        fun from(name: String): Prefs {
            return instance(name)
        }

        inline fun from(name: String, function: Prefs.() -> Unit) {
            function.invoke(instance(name))
        }

        inline fun fromDefault(function: Prefs.() -> Unit) {
            function.invoke(instance(null))
        }

        fun fromDefault(): Prefs {
            return instance(null)
        }

        @Suppress("UNCHECKED_CAST")
        operator fun set(key: String, value: Any?): Boolean {
            return defaultPreference.set(key, value)
        }

        fun setAndGet(key: String, value: Any?): Any? {
            set(key, value)
            return value
        }

        fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
            return defaultPreference.getBoolean(key, defaultValue)
        }

        fun getInt(key: String, defaultValue: Int = 0): Int {
            return defaultPreference.getInt(key, defaultValue)
        }

        fun getLong(key: String, defaultValue: Long = 0): Long {
            return defaultPreference.getLong(key, defaultValue)
        }

        fun getFloat(key: String, defaultValue: Float = 0f): Float {
            return defaultPreference.getFloat(key, defaultValue)
        }

        fun getString(key: String, defaultValue: String? = null): String? {
            return defaultPreference.getString(key, defaultValue)
        }

        fun getStringSet(key: String, defaultValue: HashSet<String>? = null): Set<String>? {
            val set = HashSet<String>()
            return defaultPreference.getStringSet(key, set)
        }
    }
}

val Context.defaultPreference: Prefs
    get() = Prefs.fromDefault()