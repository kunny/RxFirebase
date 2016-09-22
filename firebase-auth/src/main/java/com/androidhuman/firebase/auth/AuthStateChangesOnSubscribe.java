package com.androidhuman.firebase.auth;

import com.google.firebase.auth.FirebaseAuth;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

final class AuthStateChangesOnSubscribe implements Observable.OnSubscribe<FirebaseAuth> {

    private final FirebaseAuth instance;

    AuthStateChangesOnSubscribe(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    public void call(final Subscriber<? super FirebaseAuth> subscriber) {
        final FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(firebaseAuth);
                }
            }
        };

        instance.addAuthStateListener(listener);

        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                instance.removeAuthStateListener(listener);
            }
        }));
    }
}
