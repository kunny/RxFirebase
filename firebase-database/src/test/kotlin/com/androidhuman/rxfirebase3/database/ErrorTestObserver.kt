package com.androidhuman.rxfirebase3.database

import io.reactivex.rxjava3.observers.TestObserver

class ErrorTestObserver<T> : TestObserver<T>() {
    fun errorCount(): Int = errors.size
}
