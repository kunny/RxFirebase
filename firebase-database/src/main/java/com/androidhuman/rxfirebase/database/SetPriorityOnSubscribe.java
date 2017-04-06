package com.androidhuman.rxfirebase.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import android.support.annotation.NonNull;

import rx.Completable;
import rx.CompletableSubscriber;

final class SetPriorityOnSubscribe
        implements Completable.OnSubscribe {

    private final DatabaseReference ref;

    private final Object priority;

    SetPriorityOnSubscribe(DatabaseReference ref, Object priority) {
        this.ref = ref;
        this.priority = priority;
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

        ref.setPriority(priority)
                .addOnCompleteListener(listener);
    }
}
