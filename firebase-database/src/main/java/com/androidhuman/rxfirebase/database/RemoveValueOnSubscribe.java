package com.androidhuman.rxfirebase.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

final class RemoveValueOnSubscribe implements Observable.OnSubscribe<TaskResult> {

    private final DatabaseReference ref;

    RemoveValueOnSubscribe(DatabaseReference ref) {
        this.ref = ref;
    }

    @Override
    public void call(final Subscriber<? super TaskResult> subscriber) {
        final OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
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

        ref.removeValue()
                .addOnCompleteListener(listener);
    }
}
