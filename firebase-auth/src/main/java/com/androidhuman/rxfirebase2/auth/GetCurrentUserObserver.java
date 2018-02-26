package com.androidhuman.rxfirebase2.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;

final class GetCurrentUserObserver extends Maybe<FirebaseUser> {

    private FirebaseAuth instance;

    GetCurrentUserObserver(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(MaybeObserver<? super FirebaseUser> observer) {
        FirebaseUser user = instance.getCurrentUser();
        if (null != user) {
            observer.onSuccess(user);
        } else {
            observer.onComplete();
        }
    }
}
