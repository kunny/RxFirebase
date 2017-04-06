package com.androidhuman.rxfirebase.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import android.support.annotation.NonNull;

import java.util.Map;

import rx.Completable;
import rx.CompletableSubscriber;

final class UpdateChildrenOnSubscribe implements Completable.OnSubscribe {

    private final DatabaseReference ref;

    private final Map<String, Object> update;

    UpdateChildrenOnSubscribe(DatabaseReference ref, Map<String, Object> update) {
        this.ref = ref;
        this.update = update;
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

        ref.updateChildren(update)
                .addOnCompleteListener(listener);
    }
}
