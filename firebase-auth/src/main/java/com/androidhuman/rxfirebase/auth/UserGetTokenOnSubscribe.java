package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import android.support.annotation.NonNull;

import rx.Single;
import rx.SingleSubscriber;

final class UserGetTokenOnSubscribe
        implements Single.OnSubscribe<String> {

    private final FirebaseUser user;

    private final boolean forceRefresh;

    UserGetTokenOnSubscribe(FirebaseUser user, boolean forceRefresh) {
        this.user = user;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void call(final SingleSubscriber<? super String> subscriber) {
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
                    String token = task.getResult().getToken();
                    if (null != token) {
                        subscriber.onSuccess(token);
                    } else {
                        subscriber.onError(new IllegalStateException("Token does not exists."));
                    }
                }
            }
        };

        user.getToken(forceRefresh)
                .addOnCompleteListener(listener);
    }
}
