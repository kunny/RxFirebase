package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

final class RunTransactionOnSubscribe implements Observable.OnSubscribe<TaskResult> {

    private final DatabaseReference ref;

    private final boolean fireLocalEvents;

    private Func1<MutableData, Transaction.Result> task;

    RunTransactionOnSubscribe(
            DatabaseReference ref, boolean fireLocalEvents,
            Func1<MutableData, Transaction.Result> task) {
        this.ref = ref;
        this.fireLocalEvents = fireLocalEvents;
        this.task = task;
    }

    @Override
    public void call(final Subscriber<? super TaskResult> subscriber) {
        final Transaction.Handler handler = new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                return task.call(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                    DataSnapshot dataSnapshot) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(TaskResult.success());
                } else {
                    subscriber.onNext(TaskResult.failure(databaseError.toException()));
                }
            }
        };

        ref.runTransaction(handler, fireLocalEvents);
    }
}
