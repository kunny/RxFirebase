package com.androidhuman.firebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.firebase.auth.model.TaskResult;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

final class UserUpdatePasswordOnSubscribe implements Observable.OnSubscribe<TaskResult> {

    private final FirebaseUser user;

    private final String password;

    UserUpdatePasswordOnSubscribe(FirebaseUser user, String password) {
        this.user = user;
        this.password = password;
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

        user.updatePassword(password)
                .addOnCompleteListener(listener);
    }
}
