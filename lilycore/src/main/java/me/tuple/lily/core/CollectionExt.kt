package me.tuple.lily.core

/**
 * Created by LazyLoop.
 */

fun Collection<Any>?.isEmptyOrNull(): Boolean = this?.isEmpty() ?: true

fun <T> MutableCollection<T>.addIfNotContains(objectToAdd: T): T {
    if (!contains(objectToAdd)) {
        add(objectToAdd)
    }
    val bo: Boolean? = null
    if (bo == true) {

    }
    return objectToAdd
}