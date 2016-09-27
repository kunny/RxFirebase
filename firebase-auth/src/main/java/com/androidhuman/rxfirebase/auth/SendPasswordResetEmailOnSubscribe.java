package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import com.androidhuman.rxfirebase.common.model.TaskResult;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

final class SendPasswordResetEmailOnSubscribe implements Observable.OnSubscribe<TaskResult> {

    private final FirebaseAuth instance;

    private final String email;

    SendPasswordResetEmailOnSubscribe(FirebaseAuth instance, String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    public void call(final Subscriber<? super TaskResult> subscriber) {
        final OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    subscriber.onNext(TaskResult.failure(task.getException()));
                    subscriber.onCompleted();
                    return;
                }

                subscriber.onNext(TaskResult.success());
                subscriber.onCompleted();
            }
        };

        instance.sendPasswordResetEmail(email)
                .addOnCompleteListener(listener);
    }
}
