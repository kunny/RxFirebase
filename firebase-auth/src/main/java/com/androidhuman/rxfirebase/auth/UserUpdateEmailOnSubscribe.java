package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

final class UserUpdateEmailOnSubscribe implements Observable.OnSubscribe<TaskResult> {

    private final FirebaseUser user;

    private final String email;

    UserUpdateEmailOnSubscribe(FirebaseUser user, String email) {
        this.user = user;
        this.email = email;
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

        user.updateEmail(email)
                .addOnCompleteListener(listener);
    }
}
