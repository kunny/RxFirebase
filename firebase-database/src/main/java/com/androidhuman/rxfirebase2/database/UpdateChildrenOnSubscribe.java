package com.androidhuman.rxfirebase2.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import android.support.annotation.NonNull;

import java.util.Map;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

final class UpdateChildrenOnSubscribe implements CompletableOnSubscribe {

    private final DatabaseReference ref;

    private final Map<String, Object> update;

    UpdateChildrenOnSubscribe(DatabaseReference ref, Map<String, Object> update) {
        this.ref = ref;
        this.update = update;
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

        ref.updateChildren(update)
                .addOnCompleteListener(listener);
    }
}
