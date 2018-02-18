package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

final class SendPasswordResetEmailObserver extends Completable {

    private final FirebaseAuth instance;

    private final String email;

    SendPasswordResetEmailObserver(FirebaseAuth instance, String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.sendPasswordResetEmail(email)
                .addOnCompleteListener(listener);
    }

    static final class Listener extends OnCompleteDisposable<Void> {

        private final CompletableObserver observer;

        Listener(@NonNull CompletableObserver observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    observer.onComplete();
                }
            }
        }
    }
}
