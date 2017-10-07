package me.tuple.lily.core

import android.os.Build

/**
 * Created by LazyLoop.
 */

typealias AndroidVersion = Build.VERSION_CODES

fun isApiAbove(api: Int): Boolean = Build.VERSION.SDK_INT > api

fun isApiAndAbove(api: Int): Boolean = Build.VERSION.SDK_INT >= api

fun isApiOrBelow(api: Int): Boolean = Build.VERSION.SDK_INT <= api

fun isApiBelow(api: Int): Boolean = Build.VERSION.SDK_INT < api

fun isLollipopOrAbove(): Boolean = isApiAndAbove(AndroidVersion.LOLLIPOP)

fun isMarshMellowOrAbove(): Boolean = isApiAndAbove(AndroidVersion.M)

inline fun onApiAbove(api: Int, action: () -> Unit) {
    if (isApiAbove(api)) {
        action()
    }
}

inline fun onApiAndAbove(api: Int, action: () -> Unit) {
    if (isApiAndAbove(api)) {
        action()
    }
}

inline fun onApiOrBelow(api: Int, action: () -> Unit) {
    if (isApiOrBelow(api)) {
        action()
    }
}

inline fun onApiBelow(api: Int, action: () -> Unit) {
    if (isApiBelow(api)) {
        action()
    }
}

inline fun onLollipopOrAbove(action: () -> Unit) {
    if (isLollipopOrAbove()) {
        action()
    }
}

inline fun onMarshMellowOrAbove(action: () -> Unit) {
    if (isMarshMellowOrAbove()) {
        action()
    }
}