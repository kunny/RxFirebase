package com.androidhuman.rxfirebase3.auth;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;


class CreateUserWithEmailAndPasswordObserver extends Single<FirebaseUser> {

    private final FirebaseAuth instance;

    private final String email;

    private final String password;

    CreateUserWithEmailAndPasswordObserver(
            FirebaseAuth instance, String email, String password) {
        this.instance = instance;
        this.email = email;
        this.password = password;
    }

    @Override
    public void subscribeActual(@NonNull SingleObserver<? super FirebaseUser> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    private static final class Listener extends OnCompleteDisposable<AuthResult> {

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
