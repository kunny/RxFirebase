package com.androidhuman.rxfirebase2.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import android.support.annotation.NonNull;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

final class RemoveValueOnSubscribe implements CompletableOnSubscribe {

    private final DatabaseReference ref;

    RemoveValueOnSubscribe(DatabaseReference ref) {
        this.ref = ref;
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

        ref.removeValue()
                .addOnCompleteListener(listener);
    }
}
