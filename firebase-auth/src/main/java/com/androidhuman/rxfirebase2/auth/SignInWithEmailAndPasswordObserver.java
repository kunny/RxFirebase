package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.rxfirebase2.auth.core.OnCompleteDisposable;

import android.support.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class SignInWithEmailAndPasswordObserver extends Single<FirebaseUser> {

    private final FirebaseAuth instance;

    private final String email;

    private final String password;

    SignInWithEmailAndPasswordObserver(FirebaseAuth instance, String email, String password) {
        this.instance = instance;
        this.email = email;
        this.password = password;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super FirebaseUser> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.signInWithEmailAndPassword(email, password)
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
