package me.tuple.lily.core

import android.os.Handler
import android.os.Looper

/**
 * Created by LazyLoop.
 */

private val handler by lazy {
    Handler(Looper.getMainLooper())
}

private val mainThread: Thread by lazy {
    Looper.getMainLooper().thread
}

public val Thread.isMainThread: Boolean
    get() = this == mainThread


public inline fun safeExecute(action: () -> Unit): Boolean {
    return try {
        action.invoke()
        true
    } catch (e: Exception) {
        false
    }
}

public inline fun <T> safeExecute(action: () -> T?): T? {
    return try {
        action.invoke()
    } catch (e: Exception) {
        null
    }
}

fun runOnUI(action: () -> Unit) {
    if (Thread.currentThread().isMainThread) {
        action()
    } else {
        handler.post {
            action()
        }
    }
}

fun async(action: () -> Unit) {
    Thread(action).start()
}

fun disposableAsync(action: (Disposable) -> Unit): Disposable {
    val disposable = Disposable()
    action.invoke(disposable)
    return disposable
}

class Disposable {
    @Volatile var isDisposed: Boolean = false
    fun dispose(): Unit {
        isDisposed = true
    }
}
