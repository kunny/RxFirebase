package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

final class UserReauthenticateOnSubscribe implements Observable.OnSubscribe<TaskResult> {

    private final FirebaseUser user;

    private final AuthCredential credential;

    UserReauthenticateOnSubscribe(FirebaseUser user, AuthCredential credential) {
        this.user = user;
        this.credential = credential;
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

        user.reauthenticate(credential)
                .addOnCompleteListener(listener);
    }
}
