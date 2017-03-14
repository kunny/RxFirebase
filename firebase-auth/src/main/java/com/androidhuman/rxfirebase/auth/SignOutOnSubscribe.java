package com.androidhuman.rxfirebase.auth;

import com.google.firebase.auth.FirebaseAuth;

import rx.Completable;
import rx.CompletableSubscriber;

final class SignOutOnSubscribe
        implements Completable.OnSubscribe {

    private final FirebaseAuth instance;

    SignOutOnSubscribe(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    public void call(CompletableSubscriber subscriber) {
        instance.signOut();
        subscriber.onCompleted();
    }
}
