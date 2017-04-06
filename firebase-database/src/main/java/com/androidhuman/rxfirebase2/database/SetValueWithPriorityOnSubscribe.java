package com.androidhuman.rxfirebase2.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import android.support.annotation.NonNull;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

final class SetValueWithPriorityOnSubscribe<T> implements CompletableOnSubscribe {

    private final DatabaseReference ref;

    private final T value;

    private final Object priority;

    SetValueWithPriorityOnSubscribe(DatabaseReference ref, T value, Object priority) {
        this.ref = ref;
        this.value = value;
        this.priority = priority;
    }

    @Override
    public void subscribe(final CompletableEmitter emitter) {
        final OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!emitter.isDisposed()) {
                    if (!task.isSuccessful()) {
                        emitter.onError(task.getException());
                    } else {
                        emitter.onComplete();
                    }
                }
            }
        };

        ref.setValue(value, priority)
                .addOnCompleteListener(listener);
    }
}
