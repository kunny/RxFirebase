package com.androidhuman.rxfirebase.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rx.Observable;
import rx.Subscriber;

final class GetCurrentUserOnSubscribe
        implements Observable.OnSubscribe<FirebaseUser> {

    private FirebaseAuth instance;

    GetCurrentUserOnSubscribe(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    public void call(Subscriber<? super FirebaseUser> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            FirebaseUser user = instance.getCurrentUser();
            if (null != user) {
                subscriber.onNext(user);
            }
            subscriber.onCompleted();
        }
    }
}
