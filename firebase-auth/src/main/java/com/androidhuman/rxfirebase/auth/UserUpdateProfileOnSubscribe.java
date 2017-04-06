package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import android.support.annotation.NonNull;

import rx.Completable;
import rx.CompletableSubscriber;

final class UserUpdateProfileOnSubscribe
        implements Completable.OnSubscribe {

    private final FirebaseUser user;

    private final UserProfileChangeRequest request;

    UserUpdateProfileOnSubscribe(FirebaseUser user, UserProfileChangeRequest request) {
        this.user = user;
        this.request = request;
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

        user.updateProfile(request)
                .addOnCompleteListener(listener);
    }
}
