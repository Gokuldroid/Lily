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
class Prefs(context: Context, name: String) {

    constructor(function: Prefs.() -> Unit) : this(Contexter.context, defaultPrefName) {
        function(this)
    }

    val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    operator fun set(key: String, value: Any?): Boolean {
        if (value == null) {
            return remove(key)
        }
        return preferences.use {
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
        if (value == null) {
            remove(key)
            return null
        }
        preferences.use {
            when (value) {
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Set<*> -> {
                    putStringSet(key, value as Set<String>?)
                }
                else -> throw IllegalArgumentException("Unknown Type for preference")
            }
        }
        return value
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return preferences.getFloat(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String): String? {
        return preferences.getString(key, defaultValue)
    }

    fun getStringSet(key: String): Set<String>? {
        val set = HashSet<String>()
        return preferences.getStringSet(key, set)
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
        val defaultPreference: Prefs by lazy {
            Prefs(Contexter.context, defaultPrefName)
        }

        val defaultPrefName: String by lazy {
            PreferenceManager.getDefaultSharedPreferencesName(Contexter.context)
        }

        fun instance(name: String?): Prefs {
            if (name == null) {
                return defaultPreference
            }
            return Prefs(Contexter.context, name)
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
    }
}