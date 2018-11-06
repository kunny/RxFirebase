package com.androidhuman.rxfirebase2.auth;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

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
    protected void subscribeActual(SingleObserver<? super AuthResult> observer) {
        AuthResultListener listener = new AuthResultListener(observer);
        observer.onSubscribe(listener);

        instance.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }
}
