package com.androidhuman.rxfirebase2.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class SignInWithCredentialObserver extends Single<AuthResult> {

    private final FirebaseAuth instance;

    private final AuthCredential credential;

    SignInWithCredentialObserver(FirebaseAuth instance, AuthCredential credential) {
        this.instance = instance;
        this.credential = credential;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super AuthResult> observer) {
        AuthResultListener listener = new AuthResultListener(observer);
        observer.onSubscribe(listener);

        instance.signInWithCredential(credential)
                .addOnCompleteListener(listener);
    }

}
