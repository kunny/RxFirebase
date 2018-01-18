package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import com.androidhuman.rxfirebase2.core.SimpleDisposable;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Observer;

final class QueryChildEventsObserver extends Observable<ChildEvent> {

    private Query instance;

    QueryChildEventsObserver(Query instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(Observer<? super ChildEvent> observer) {
        Listener listener = new Listener(instance, observer);
        observer.onSubscribe(listener);

        instance.addChildEventListener(listener);
    }

    static final class Listener extends SimpleDisposable implements ChildEventListener {

        private final Query query;

        private final Observer<? super ChildEvent> observer;

        Listener(@NonNull Query query,
                @NonNull Observer<? super ChildEvent> observer) {
            this.query = query;
            this.observer = observer;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            if (!isDisposed()) {
                observer.onNext(ChildAddEvent.create(dataSnapshot, previousChildName));
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            if (!isDisposed()) {
                observer.onNext(ChildChangeEvent.create(dataSnapshot, previousChildName));
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            if (!isDisposed()) {
                observer.onNext(ChildRemoveEvent.create(dataSnapshot));
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            if (!isDisposed()) {
                observer.onNext(ChildMoveEvent.create(dataSnapshot, previousChildName));
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
