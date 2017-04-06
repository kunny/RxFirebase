package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.support.annotation.NonNull;

import rx.Completable;
import rx.CompletableSubscriber;

final class SendPasswordResetEmailOnSubscribe
        implements Completable.OnSubscribe {

    private final FirebaseAuth instance;

    private final String email;

    SendPasswordResetEmailOnSubscribe(FirebaseAuth instance, String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    public void call(final CompletableSubscriber subscriber) {
        final OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    subscriber.onError(task.getException());
                } else {
                    subscriber.onCompleted();
                }
            }
        };

        instance.sendPasswordResetEmail(email)
                .addOnCompleteListener(listener);
    }
}
