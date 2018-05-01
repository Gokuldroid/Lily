package me.tuple.lily.core

/**
 * Created by LazyLoop.
 */
inline fun unless(condition: Boolean, action: () -> Unit): Boolean {
    if (condition.not()) {
        action.invoke()
    }
    return condition
}

inline fun Boolean.onFalse(action: () -> Unit): Boolean {
    if (not()) {
        action.invoke()
    }
    return this
}

inline fun Boolean.onTrue(action: () -> Unit): Boolean {
    if (this) {
        action.invoke()
    }
    return this
}


fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)