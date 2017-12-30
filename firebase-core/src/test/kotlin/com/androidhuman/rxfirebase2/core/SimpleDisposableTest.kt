package com.androidhuman.rxfirebase2.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class SimpleDisposableTest {

    @Test
    fun disposedState() {
        val disposable = object: SimpleDisposable() {
            override fun onDispose() {
                // do nothing
            }
        }

        assertThat(disposable.isDisposed).isFalse
        disposable.dispose()
        assertThat(disposable.isDisposed).isTrue
    }

    @Test
    fun unsubscribeTwiceDoesNotRunTwice() {
        val called = AtomicInteger(0)

        val disposable = object: SimpleDisposable() {
            override fun onDispose() {
                called.incrementAndGet()
            }
        }

        with(disposable) {
            dispose(); dispose()
        }

        assertThat(called.get()).isEqualTo(1)
    }
}