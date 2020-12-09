package com.androidhuman.rxfirebase3.auth;

import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;

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
