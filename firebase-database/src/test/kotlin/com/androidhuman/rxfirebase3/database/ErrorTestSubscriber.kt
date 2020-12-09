package com.androidhuman.rxfirebase3.database

import io.reactivex.rxjava3.subscribers.TestSubscriber

class ErrorTestSubscriber<T> : TestSubscriber<T>() {
    fun errorCount(): Int = errors.size
}
