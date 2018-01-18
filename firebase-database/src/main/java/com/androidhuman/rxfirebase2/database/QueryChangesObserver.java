package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.androidhuman.rxfirebase2.core.SimpleDisposable;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Observer;

final class QueryChangesObserver extends Observable<DataSnapshot> {

    private final Query instance;

    QueryChangesObserver(Query instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(Observer<? super DataSnapshot> observer) {
        Listener listener = new Listener(instance, observer);
        observer.onSubscribe(listener);

        instance.addValueEventListener(listener);
    }

    static final class Listener extends SimpleDisposable implements ValueEventListener {

        private final Query query;

        private final Observer<? super DataSnapshot> observer;

        Listener(@NonNull Query query, @NonNull Observer<? super DataSnapshot> observer) {
            this.query = query;
            this.observer = observer;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!isDisposed()) {
                observer.onNext(dataSnapshot);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            if (!isDisposed()) {
                observer.onError(databaseError.toException());
            }
        }

        @Override
        protected void onDispose() {
            query.removeEventListener(this);
        }
    }
}
