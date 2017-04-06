package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.functions.Function;

final class RunTransactionOnSubscribe implements CompletableOnSubscribe {

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
    public void subscribe(final CompletableEmitter emitter) {
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
                        emitter.onComplete();
                    } else {
                        emitter.onError(databaseError.toException());
                    }
                }
            }
        };

        ref.runTransaction(handler, fireLocalEvents);
    }
}
