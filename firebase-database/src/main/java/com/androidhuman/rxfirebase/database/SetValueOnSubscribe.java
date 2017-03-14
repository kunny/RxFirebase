package com.androidhuman.rxfirebase.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import android.support.annotation.NonNull;

import rx.Completable;
import rx.CompletableSubscriber;

final class SetValueOnSubscribe<T>
        implements Completable.OnSubscribe {

    private final DatabaseReference ref;

    private final T value;

    SetValueOnSubscribe(DatabaseReference ref, T value) {
        this.ref = ref;
        this.value = value;
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

        ref.setValue(value)
                .addOnCompleteListener(listener);
    }
}
