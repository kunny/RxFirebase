package com.androidhuman.firebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

final class UserGetTokenOnSubscribe implements Observable.OnSubscribe<String> {

    private final FirebaseUser user;

    private final boolean forceRefresh;

    UserGetTokenOnSubscribe(FirebaseUser user, boolean forceRefresh) {
        this.user = user;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void call(final Subscriber<? super String> subscriber) {
        OnCompleteListener<GetTokenResult> listener = new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (!task.isSuccessful()) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(task.getException());
                    }
                    return;
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(task.getResult().getToken());
                    subscriber.onCompleted();
                }
            }
        };

        user.getToken(forceRefresh)
                .addOnCompleteListener(listener);
    }
}
