package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import rx.Completable;
import rx.CompletableSubscriber;

final class UserReauthenticateOnSubscribe
        implements Completable.OnSubscribe {

    private final FirebaseUser user;

    private final AuthCredential credential;

    UserReauthenticateOnSubscribe(FirebaseUser user, AuthCredential credential) {
        this.user = user;
        this.credential = credential;
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

        user.reauthenticate(credential)
                .addOnCompleteListener(listener);
    }
}
