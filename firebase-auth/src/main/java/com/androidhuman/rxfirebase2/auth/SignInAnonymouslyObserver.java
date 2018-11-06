package com.androidhuman.rxfirebase2.auth;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class SignInAnonymouslyObserver extends Single<AuthResult> {

    private final FirebaseAuth instance;

    SignInAnonymouslyObserver(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super AuthResult> observer) {
        AuthResultListener listener = new AuthResultListener(observer);
        observer.onSubscribe(listener);

        instance.signInAnonymously()
                .addOnCompleteListener(listener);
    }

}
