package com.androidhuman.rxfirebase2.auth;

import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

final class SignOutObserver extends Completable {

    private final FirebaseAuth instance;

    SignOutObserver(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        instance.signOut();
        observer.onComplete();
    }
}
