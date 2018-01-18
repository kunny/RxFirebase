package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import com.androidhuman.rxfirebase2.core.SimpleDisposable;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.functions.Function;

final class RunTransactionObserver extends Completable {

    private final DatabaseReference instance;

    private final boolean fireLocalEvents;

    private Function<MutableData, Transaction.Result> task;

    RunTransactionObserver(
            DatabaseReference instance, boolean fireLocalEvents,
            Function<MutableData, Transaction.Result> task) {
        this.instance = instance;
        this.fireLocalEvents = fireLocalEvents;
        this.task = task;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        Listener listener = new Listener(task, observer);
        observer.onSubscribe(listener);

        instance.runTransaction(listener, fireLocalEvents);
    }

    static final class Listener extends SimpleDisposable implements Transaction.Handler {

        private final Function<MutableData, Transaction.Result> task;

        private final CompletableObserver observer;

        Listener(@NonNull Function<MutableData, Transaction.Result> task,
                @NonNull CompletableObserver observer) {
            this.task = task;
            this.observer = observer;
        }

        @Override
        public Transaction.Result doTransaction(MutableData mutableData) {
            try {
                return this.task.apply(mutableData);
            } catch (Exception e) {
                return Transaction.abort();
            }
        }

        @Override
        public void onComplete(DatabaseError databaseError,
                boolean committed, DataSnapshot dataSnapshot) {
            if (!isDisposed()) {
                if (null != databaseError) {
                    observer.onError(databaseError.toException());
                } else {
                    observer.onComplete();
                }
            }
        }

        @Override
        protected void onDispose() {
            // do nothing
        }
    }
}
