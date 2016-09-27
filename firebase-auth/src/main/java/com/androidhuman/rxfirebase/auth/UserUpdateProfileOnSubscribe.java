package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

final class UserUpdateProfileOnSubscribe implements Observable.OnSubscribe<TaskResult> {

    private final FirebaseUser user;

    private final UserProfileChangeRequest request;

    UserUpdateProfileOnSubscribe(FirebaseUser user, UserProfileChangeRequest request) {
        this.user = user;
        this.request = request;
    }

    @Override
    public void call(final Subscriber<? super TaskResult> subscriber) {
        OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!subscriber.isUnsubscribed()) {
                    if (!task.isSuccessful()) {
                        subscriber.onNext(TaskResult.failure(task.getException()));
                    } else {
                        subscriber.onNext(TaskResult.success());
                    }
                    subscriber.onCompleted();
                }
            }
        };

        user.updateProfile(request)
                .addOnCompleteListener(listener);
    }
}
