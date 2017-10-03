package me.tuple.lily.core

import android.os.Build

/**
 * Created by LazyLoop.
 */

public fun isApiAndAbove(api: Int): Boolean = Build.VERSION.SDK_INT >= api

public fun isApiOrBelow(api: Int): Boolean = Build.VERSION.SDK_INT <= api

public fun isApiBelow(api: Int): Boolean = Build.VERSION.SDK_INT < api

public fun isLollipopOrAbove(): Boolean = isApiAndAbove(Build.VERSION_CODES.LOLLIPOP)

public fun isMarshMellowOrAbove(): Boolean = isApiAndAbove(Build.VERSION_CODES.M)

inline fun onLollipopAbove(action: () -> Unit) {
    if (isLollipopOrAbove()) {
        action()
    }
}

inline fun onMarshMellowAbove(action: () -> Unit) {
    if (isMarshMellowOrAbove()) {
        action()
    }
}