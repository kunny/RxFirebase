package com.androidhuman.rxfirebase3.auth;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;

final class GetCurrentUserObserver extends Maybe<FirebaseUser> {

    private final FirebaseAuth instance;

    GetCurrentUserObserver(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(@NonNull MaybeObserver<? super FirebaseUser> observer) {
        FirebaseUser user = instance.getCurrentUser();
        if (null != user) {
            observer.onSuccess(user);
        } else {
            observer.onComplete();
        }
    }
}
