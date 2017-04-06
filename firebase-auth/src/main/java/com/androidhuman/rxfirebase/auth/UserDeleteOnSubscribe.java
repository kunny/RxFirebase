package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import rx.Completable;
import rx.CompletableSubscriber;

final class UserDeleteOnSubscribe
        implements Completable.OnSubscribe {

    private final FirebaseUser user;

    UserDeleteOnSubscribe(FirebaseUser user) {
        this.user = user;
    }

    @Override
    public void call(final CompletableSubscriber subscriber) {
        OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    subscriber.onError(task.getException());
                } else {
                    subscriber.onCompleted();
                }

            }
        };

        user.delete()
                .addOnCompleteListener(listener);
    }
}
