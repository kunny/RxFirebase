package com.androidhuman.rxfirebase3.auth;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;

final class SignInWithEmailAndPasswordObserver extends Single<AuthResult> {

    private final FirebaseAuth instance;

    private final String email;

    private final String password;

    SignInWithEmailAndPasswordObserver(FirebaseAuth instance, String email, String password) {
        this.instance = instance;
        this.email = email;
        this.password = password;
    }

    @Override
    protected void subscribeActual(@NonNull final SingleObserver<? super AuthResult> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    private static final class Listener extends OnCompleteDisposable<AuthResult> {

        private final SingleObserver<? super AuthResult> observer;

        Listener(@NonNull SingleObserver<? super AuthResult> observer) {
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
}
