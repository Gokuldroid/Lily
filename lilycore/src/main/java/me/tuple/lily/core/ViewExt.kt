package me.tuple.lily.core

import android.app.Activity
import android.content.Context
import androidx.annotation.IdRes
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager


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

fun View?.hide() {
    this?.visibility = View.INVISIBLE
}

fun View?.setPadding(padding: Int) {
    this?.apply {
        this.setPadding(padding, padding, padding, padding)
    }
}

fun View?.setPaddingLeft(padding: Int) {
    this?.apply {
        this.setPadding(padding, paddingTop, paddingRight, paddingBottom)
    }
}

fun View?.setPaddingTop(padding: Int) {
    this?.apply {
        this.setPadding(paddingLeft, padding, paddingRight, paddingBottom)
    }
}

fun View?.setPaddingRight(padding: Int) {
    this?.apply {
        this.setPadding(paddingLeft, paddingTop, padding, paddingBottom)
    }
}

fun View?.setPaddingBottom(padding: Int) {
    this?.apply {
        this.setPadding(paddingLeft, paddingTop, paddingRight, padding)
    }
}

fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { findViewById<View>(idRes) as T }
}

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { findViewById<View>(idRes) as T }
}

@Suppress("UNCHECKED_CAST")
fun <T : View> View.findById(@IdRes idRes: Int): T = findViewById<View>(idRes) as T

@Suppress("UNCHECKED_CAST")
fun <T : View> Activity.findById(@IdRes idRes: Int): T = findViewById<View>(idRes) as T

fun Activity.onClick(@IdRes id: Int, function: (View) -> Unit): View {
    val view = findById<View>(id)
    view.setOnClickListener(function)
    return view
}

fun View.onClick(@IdRes id: Int, function: (View) -> Unit) {
    findById<View>(id).setOnClickListener(function)
}

fun View.getViewYPos(): Int {
    val location = IntArray(2)
    location[0] = 0
    location[1] = this.y.toInt()
    (this.parent as View).getLocationInWindow(location)
    return location[1]
}

fun View.getViewXPos(): Int {
    val location = IntArray(2)
    location[0] = 0
    location[1] = this.y.toInt()
    (this.parent as View).getLocationInWindow(location)
    return location[0]
}

fun View.getViewPos(): IntArray {
    val location = IntArray(2)
    location[0] = 0
    location[1] = this.y.toInt()
    (this.parent as View).getLocationInWindow(location)
    return location
}