package com.androidhuman.rxfirebase3.core

import com.google.android.gms.tasks.OnCompleteListener

abstract class OnCompleteDisposable<T> : SimpleDisposable(), OnCompleteListener<T> {

    override fun onDispose() {
        // do nothing
    }
}
