package com.androidhuman.firebase.auth;

import com.google.firebase.auth.FirebaseAuth;

import com.androidhuman.firebase.auth.model.TaskResult;

import rx.Observable;
import rx.Subscriber;

final class SignOutOnSubscribe implements Observable.OnSubscribe<TaskResult> {

    private final FirebaseAuth instance;

    SignOutOnSubscribe(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    public void call(Subscriber<? super TaskResult> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            instance.signOut();
            subscriber.onNext(TaskResult.success());
            subscriber.onCompleted();
        }
    }
}
