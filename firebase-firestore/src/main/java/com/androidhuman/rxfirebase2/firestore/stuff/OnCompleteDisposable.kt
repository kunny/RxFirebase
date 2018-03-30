package com.androidhuman.rxfirebase2.firestore.stuff

import com.google.android.gms.tasks.OnCompleteListener

abstract class OnCompleteDisposable<T> : SimpleDisposable(), OnCompleteListener<T> {

    override fun onDispose() {
        // do nothing
    }
}