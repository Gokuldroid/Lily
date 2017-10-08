package me.tuple.lily.core

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View
import android.view.ViewGroup

/**
 * Created by LazyLoop.
 */

inline fun ViewGroup.forEach(a: (View) -> Unit) {
    val count = this.childCount
    for (i in 0 until count) {
        a(this.getChildAt(i))
    }
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.show() {
    this?.visibility = View.VISIBLE
}

fun View?.setPadding(padding: Int) {
    this?.apply {
        this.setPadding(padding, padding, padding, padding)
    }
}

fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { findViewById(idRes) as T }
}

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { findViewById(idRes) as T }
}

@Suppress("UNCHECKED_CAST")
fun <T : View> View.findById(@IdRes idRes: Int): T = findViewById(idRes) as T

@Suppress("UNCHECKED_CAST")
fun <T : View> Activity.findById(@IdRes idRes: Int): T = findViewById(idRes) as T

fun Activity.onClick(@IdRes id: Int, function: (View) -> Unit): View {
    val view = findById<View>(id)
    view.setOnClickListener(function)
    return view
}

fun View.onClick(@IdRes id: Int, function: (View) -> Unit) {
    findById<View>(id).setOnClickListener(function)
}

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)