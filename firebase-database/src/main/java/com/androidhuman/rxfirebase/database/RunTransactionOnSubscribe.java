package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Function;

final class RunTransactionOnSubscribe implements SingleOnSubscribe<TaskResult> {

    private final DatabaseReference ref;

    private final boolean fireLocalEvents;

    private Function<MutableData, Transaction.Result> task;

    RunTransactionOnSubscribe(
            DatabaseReference ref, boolean fireLocalEvents,
            Function<MutableData, Transaction.Result> task) {
        this.ref = ref;
        this.fireLocalEvents = fireLocalEvents;
        this.task = task;
    }

    @Override
    public void subscribe(final SingleEmitter<TaskResult> emitter) {
        final Transaction.Handler handler = new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                try {
                    return task.apply(mutableData);
                } catch (Exception e) {
                    //TODO: Is this enough?
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                    DataSnapshot dataSnapshot) {
                if (!emitter.isDisposed()) {
                    if (null == databaseError) {
                        emitter.onSuccess(TaskResult.success());
                    } else {
                        emitter.onSuccess(TaskResult.failure(databaseError.toException()));
                    }
                }
            }
        };

        ref.runTransaction(handler, fireLocalEvents);
    }
}
