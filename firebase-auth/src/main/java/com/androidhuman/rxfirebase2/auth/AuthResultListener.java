package com.androidhuman.rxfirebase2.auth;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import io.reactivex.SingleObserver;

final class AuthResultListener extends OnCompleteDisposable<AuthResult> {

    private final SingleObserver<? super AuthResult> observer;

    AuthResultListener(@NonNull SingleObserver<? super AuthResult> observer) {
        this.observer = observer;
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (!isDisposed()) {
            if (!task.isSuccessful()) {
                observer.onError(task.getException());
            } else {
                observer.onSuccess(task.getResult());
            }
        }
    }
}
