package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import rx.Completable;
import rx.CompletableSubscriber;

final class UserUpdateEmailOnSubscribe
        implements Completable.OnSubscribe {

    private final FirebaseUser user;

    private final String email;

    UserUpdateEmailOnSubscribe(FirebaseUser user, String email) {
        this.user = user;
        this.email = email;
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

        user.updateEmail(email)
                .addOnCompleteListener(listener);
    }
}
