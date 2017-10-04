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
@SuppressLint("CommitPrefEdits")
class Prefs(context: Context, name: String) {

    constructor(function: Prefs.() -> Unit) : this(Contexter.context, defaultPrefName) {
        function(this)
    }

    val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    operator fun set(key: String, value: Boolean): Boolean {
        return preferences.edit()
                .putBoolean(key, value)
                .commit()
    }

    operator fun set(key: String, value: Int): Boolean {
        return preferences.edit()
                .putInt(key, value)
                .commit()
    }

    operator fun set(key: String, value: Long): Boolean {
        return preferences.edit()
                .putLong(key, value)
                .commit()
    }

    operator fun set(key: String, value: Float): Boolean {
        return preferences.edit()
                .putFloat(key, value)
                .commit()
    }

    operator fun set(key: String, value: String): Boolean {
        return preferences.edit()
                .putString(key, value)
                .commit()
    }

    operator fun set(key: String, value: Set<String>): Boolean {
        return preferences.edit()
                .putStringSet(key, value)
                .commit()
    }

    fun setAndGet(key: String, value: Boolean): Boolean {
        preferences.edit()
                .putBoolean(key, value)
                .commit()
        return value
    }


    fun setAndGet(key: String, value: Int): Int {
        preferences.edit()
                .putInt(key, value)
                .commit()
        return value
    }

    fun setAndGet(key: String, value: Long): Long {
        preferences.edit()
                .putLong(key, value)
                .commit()
        return value
    }

    fun setAndGet(key: String, value: Float): Float {
        preferences.edit()
                .putFloat(key, value)
                .commit()
        return value
    }

    fun setAndGet(key: String, value: String?): String? {
        preferences.edit()
                .putString(key, value)
                .commit()
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
        return preferences.edit()
                .remove(key)
                .commit()
    }

    fun clear(): Boolean {
        return preferences.edit()
                .clear()
                .commit()
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

