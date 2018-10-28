package me.tuple.lily.core

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.os.Handler
import android.os.Looper
import me.tuple.lilycore.BuildConfig
import java.lang.ref.WeakReference
import java.util.concurrent.Executor
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

val Thread.isMainThread: Boolean
    get() = this == mainThread


inline fun safeExecute(action: () -> Unit): Boolean {
    return try {
        action.invoke()
        true
    } catch (e: Exception) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
        false
    }
}

inline fun <T> safeExecute(action: () -> T?): ExecutionResult<T> {
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

fun <T> Any.async(action: AsyncContext.() -> T) {
    val asyncContext = AsyncContext(WeakReference(this))
    AppExecutors.network.submit {
        if (!asyncContext.isDisposed) {
            action.invoke(asyncContext)
        }
    }
}

fun <T> Any.asyncNetwork(action: AsyncContext.() -> T) {
    async(action)
}

fun <T> Any.asyncIO(action: AsyncContext.() -> T) {
    val asyncContext = AsyncContext(WeakReference(this))
    AppExecutors.io.submit {
        if (!asyncContext.isDisposed) {
            action.invoke(asyncContext)
        }
    }
}

class AsyncContext(val weakRef: WeakReference<Any>) {
    @Volatile
    var isDisposed = false
        get() {
            val context = weakRef.get() ?: return true
            if (context is LifecycleOwner && !context.isCreated()) {
                return true
            }
            return false
        }

    fun dispose() {
        isDisposed = true
    }
}

object AppExecutors {
    val network: ExecutorService = Executors.newFixedThreadPool(3)
    val io: ExecutorService = Executors.newSingleThreadExecutor()
    val main: MainThreadExecutor = MainThreadExecutor()
}

class MainThreadExecutor {
    fun submit(task: () -> Unit) = handler.post(task)
}

fun safeSleep(mills: Long): Boolean = safeExecute { Thread.sleep(mills) }

fun LifecycleOwner.isCreated(): Boolean = lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)

fun LifecycleOwner.isStarted(): Boolean = lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)