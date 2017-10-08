package me.tuple.lily.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import me.tuple.lily.core.Contexter
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

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

    private val preferences: SharedPreferences by lazy {
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
                is Float -> putFloat(key, value)
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

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
            preferences.getBoolean(key, defaultValue)

    fun getInt(key: String, defaultValue: Int = 0): Int =
            preferences.getInt(key, defaultValue)

    fun getLong(key: String, defaultValue: Long = 0): Long =
            preferences.getLong(key, defaultValue)

    fun getFloat(key: String, defaultValue: Float = 0f): Float =
            preferences.getFloat(key, defaultValue)

    fun getString(key: String, defaultValue: String? = null): String? =
            preferences.getString(key, defaultValue)

    fun getStringSet(key: String, defaultValue: HashSet<String>? = null): Set<String>? =
            preferences.getStringSet(key, defaultValue)

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

    operator fun contains(key: String): Boolean = preferences.contains(key)

    companion object {
        private val defaultPreference: Prefs by lazy {
            Prefs(Contexter.context, defaultPrefName)
        }

        private val defaultPrefName: String by lazy {
            Contexter.context.packageName + "_preferences"
        }

        fun instance(name: String?): Prefs =
                if (name == null) defaultPreference else Prefs(Contexter.context, name)

        fun from(name: String): Prefs = instance(name)

        inline fun from(name: String, function: Prefs.() -> Unit) {
            function.invoke(instance(name))
        }

        inline fun fromDefault(function: Prefs.() -> Unit) {
            function.invoke(instance(null))
        }

        fun fromDefault(): Prefs = instance(null)

        @Suppress("UNCHECKED_CAST")
        operator fun set(key: String, value: Any?): Boolean = defaultPreference.set(key, value)

        fun setAndGet(key: String, value: Any?): Any? {
            set(key, value)
            return value
        }

        fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
                defaultPreference.getBoolean(key, defaultValue)

        fun getInt(key: String, defaultValue: Int = 0): Int =
                defaultPreference.getInt(key, defaultValue)

        fun getLong(key: String, defaultValue: Long = 0): Long =
                defaultPreference.getLong(key, defaultValue)

        fun getFloat(key: String, defaultValue: Float = 0f): Float =
                defaultPreference.getFloat(key, defaultValue)

        fun getString(key: String, defaultValue: String? = null): String? =
                defaultPreference.getString(key, defaultValue)

        fun getStringSet(key: String, defaultValue: HashSet<String>? = null): Set<String>? =
                defaultPreference.getStringSet(key, defaultValue)

        fun remove(key: String): Boolean = defaultPreference.remove(key)

        fun clear(): Boolean = defaultPreference.clear()
    }
}

val Context.defaultPreference: Prefs
    get() = Prefs.fromDefault()

inline fun <reified T, P> delegate(key: String, defaultValue: T, prefs: Prefs): ReadWriteProperty<P, T> =
        object : ReadWriteProperty<P, T> {
            override fun getValue(thisRef: P, property: KProperty<*>): T =
                    when (T::class) {
                        String::class -> prefs.getString(key, defaultValue as? String) as T
                        java.lang.Integer::class -> prefs.getInt(key, defaultValue as? Int ?: 0) as T
                        java.lang.Boolean::class -> prefs.getBoolean(key, defaultValue as? Boolean ?: false) as T
                        java.lang.Float::class -> prefs.getFloat(key, defaultValue as? Float ?: 0f) as T
                        java.lang.Long::class -> prefs.getLong(key, defaultValue as? Long ?: 0L) as T
                        else -> throw UnsupportedOperationException("Not yet implemented")
                    }

            override fun setValue(thisRef: P, property: KProperty<*>, value: T) {
                Prefs[key] = value
            }
        }

fun <P> stringPreference(key: String, defaultValue: String? = null): ReadWriteProperty<P, String?> =
        delegate<String?, P>(key, defaultValue, Prefs.fromDefault())

fun <P> intPreference(key: String, defaultValue: Int = 0): ReadWriteProperty<P, Int> =
        delegate<Int, P>(key, defaultValue, Prefs.fromDefault())

fun <P> booleanPreference(key: String, defaultValue: Boolean = false): ReadWriteProperty<P, Boolean> =
        delegate<Boolean, P>(key, defaultValue, Prefs.fromDefault())

fun <P> floatPreference(key: String, defaultValue: Float = 0f): ReadWriteProperty<P, Float> =
        delegate<Float, P>(key, defaultValue, Prefs.fromDefault())

fun <P> longPreference(key: String, defaultValue: Long = 0L): ReadWriteProperty<P, Long> =
        delegate<Long, P>(key, defaultValue, Prefs.fromDefault())
