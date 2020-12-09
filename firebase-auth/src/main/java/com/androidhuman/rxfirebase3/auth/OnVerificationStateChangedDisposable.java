package com.androidhuman.rxfirebase3.auth;

import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.disposables.Disposable;

abstract class OnVerificationStateChangedDisposable
        extends PhoneAuthProvider.OnVerificationStateChangedCallbacks implements Disposable {

    private final AtomicBoolean unsubscribed = new AtomicBoolean();

    @Override
    public boolean isDisposed() {
        return unsubscribed.get();
    }

    @Override
    public void dispose() {
        if (unsubscribed.compareAndSet(false, true)) {
            onDispose();
        }
    }

    private void onDispose() {
        // do nothing
    }
}
