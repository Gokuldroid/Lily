package me.tuple.lily.core

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.annotation.IdRes
import android.view.View
import android.view.ViewGroup

/**
 * Created by LazyLoop.
 */

inline fun ViewGroup.forEach(a: (View) -> Unit) {
    val count = this.childCount
    for (i in 0..count - 1) {
        a(this.getChildAt(i))
    }
}

fun View?.hideView() {
    this?.visibility = View.GONE
}

fun View?.showView() {
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
private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)