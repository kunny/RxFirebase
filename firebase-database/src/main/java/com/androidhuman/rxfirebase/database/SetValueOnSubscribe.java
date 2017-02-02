package com.androidhuman.rxfirebase.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import android.support.annotation.NonNull;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

final class SetValueOnSubscribe<T> implements CompletableOnSubscribe {

    private final DatabaseReference ref;

    private final T value;

    SetValueOnSubscribe(DatabaseReference ref, T value) {
        this.ref = ref;
        this.value = value;
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

        ref.setValue(value)
                .addOnCompleteListener(listener);
    }
}
