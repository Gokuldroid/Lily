package me.tuple.lily.core

import android.app.Activity
import android.app.Fragment
import android.os.Handler
import android.os.Looper
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import android.support.v4.app.Fragment as SupportFragment

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

public inline fun <T> safeExecute(action: () -> T?): ExecutionResult<T> {
    return try {
        ExecutionResult(action.invoke(), null)
    } catch (e: Exception) {
        ExecutionResult(null, e)
    }
}

class ExecutionResult<out T>(val result: T?, private val exception: Exception?) {
    var isSuccessful = false
        get() = exception == null
}

fun AsyncContext.runOnUI(action: () -> Unit) {
    weakRef.get()?.also {
        if (!isDisposed) {
            if (Thread.currentThread().isMainThread) {
                action()
            } else {
                handler.post {
                    action()
                }
            }
        }
    }
}

fun <T> Any.async(action: AsyncContext.() -> T): Future<T> {
    val asyncContext = AsyncContext(WeakReference(this))
    return BackgroundExecutor.submit {
        action.invoke(asyncContext)
    }
}

class AsyncContext(val weakRef: WeakReference<Any>) {
    @Volatile
    var isDisposed = false
        get() {
            val context = weakRef.get() ?: return true
            if (context is Activity && context.isFinishing) {
                return true
            }
            if (context is Fragment && context.isDetached) {
                return true
            }
            if (context is SupportFragment && context.isDetached) {
                return true
            }
            return false
        }

    fun dispose() {
        isDisposed = true
    }
}

internal object BackgroundExecutor {
    private var executor: ExecutorService =
            Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors())

    fun <T> submit(task: () -> T): Future<T> = executor.submit(task)
}