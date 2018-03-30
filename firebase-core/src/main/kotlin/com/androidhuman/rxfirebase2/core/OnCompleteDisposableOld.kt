package com.androidhuman.rxfirebase2.core

import com.google.android.gms.tasks.OnCompleteListener

abstract class OnCompleteDisposableOld<T> : SimpleDisposableOld(), OnCompleteListener<T> {

    override fun onDispose() {
        // do nothing
    }
}