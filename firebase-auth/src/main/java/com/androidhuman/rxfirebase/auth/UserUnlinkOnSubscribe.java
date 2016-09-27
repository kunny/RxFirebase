package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

final class UserUnlinkOnSubscribe implements Observable.OnSubscribe<AuthResult> {

    private final FirebaseUser user;

    private final String provider;

    UserUnlinkOnSubscribe(FirebaseUser user, String provider) {
        this.user = user;
        this.provider = provider;
    }

    @Override
    public void call(final Subscriber<? super AuthResult> subscriber) {
        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(task.getException());
                    }
                    return;
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(task.getResult());
                    subscriber.onCompleted();
                }
            }
        };

        user.unlink(provider)
                .addOnCompleteListener(listener);
    }
}
