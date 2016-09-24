package com.androidhuman.firebase.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.memoizrlabs.retrooptional.Optional;

import rx.Observable;
import rx.Subscriber;

final class GetCurrentUserOnSubscribe implements Observable.OnSubscribe<Optional<FirebaseUser>> {

    private FirebaseAuth instance;

    GetCurrentUserOnSubscribe(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    public void call(Subscriber<? super Optional<FirebaseUser>> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(Optional.of(instance.getCurrentUser()));
            subscriber.onCompleted();
        }
    }
}
