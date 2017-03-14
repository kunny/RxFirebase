package com.androidhuman.rxfirebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import rx.Completable;
import rx.CompletableSubscriber;
import rx.functions.Func1;

final class RunTransactionOnSubscribe implements Completable.OnSubscribe {

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
    public void call(final CompletableSubscriber subscriber) {
        final Transaction.Handler handler = new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                return task.call(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                    DataSnapshot dataSnapshot) {

                if (null == databaseError) {
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(databaseError.toException());
                }
            }
        };

        ref.runTransaction(handler, fireLocalEvents);
    }
}
