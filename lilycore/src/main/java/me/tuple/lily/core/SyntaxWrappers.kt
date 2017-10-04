package me.tuple.lily.core

/**
 * Created by LazyLoop.
 */
inline fun unless(condition: Boolean, action: () -> Unit) {
    if (condition.not()) {
        action.invoke()
    }
}