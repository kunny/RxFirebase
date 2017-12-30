package com.androidhuman.rxfirebase2.core

import io.reactivex.disposables.Disposable
import java.util.concurrent.atomic.AtomicBoolean

abstract class SimpleDisposable : Disposable {

    private val unsubscribed = AtomicBoolean()

    override fun isDisposed() = unsubscribed.get()

    override fun dispose() {
        if (unsubscribed.compareAndSet(false, true)) {
            onDispose()
        }
    }

    abstract protected fun onDispose()
}
