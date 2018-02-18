package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;

import android.support.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class SignInAnonymouslyObserver extends Single<FirebaseUser> {

    private final FirebaseAuth instance;

    SignInAnonymouslyObserver(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super FirebaseUser> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.signInAnonymously()
                .addOnCompleteListener(listener);
    }

    static final class Listener extends OnCompleteDisposable<AuthResult> {

        private final SingleObserver<? super FirebaseUser> observer;

        Listener(@NonNull SingleObserver<? super FirebaseUser> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    observer.onSuccess(task.getResult().getUser());
                }
            }
        }
    }
}
