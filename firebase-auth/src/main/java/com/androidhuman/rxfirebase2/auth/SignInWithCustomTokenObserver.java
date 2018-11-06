package com.androidhuman.rxfirebase2.auth;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class SignInWithCustomTokenObserver extends Single<AuthResult> {

    private final FirebaseAuth instance;

    private final String token;

    SignInWithCustomTokenObserver(FirebaseAuth instance, String token) {
        this.instance = instance;
        this.token = token;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super AuthResult> observer) {
        AuthResultListener listener = new AuthResultListener(observer);
        observer.onSubscribe(listener);

        instance.signInWithCustomToken(token)
                .addOnCompleteListener(listener);
    }
}
