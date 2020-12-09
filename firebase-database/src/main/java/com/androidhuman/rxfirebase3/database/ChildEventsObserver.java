package com.androidhuman.rxfirebase3.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.androidhuman.rxfirebase3.core.SimpleDisposable;

import android.support.annotation.NonNull;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

final class ChildEventsObserver extends Observable<ChildEvent> {

    private final DatabaseReference instance;

    ChildEventsObserver(DatabaseReference instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(@NonNull final Observer<? super ChildEvent> observer) {
        Listener listener = new Listener(instance, observer);
        observer.onSubscribe(listener);

        instance.addChildEventListener(listener);
    }

    static final class Listener extends SimpleDisposable implements ChildEventListener {

        private final DatabaseReference ref;

        private final Observer<? super ChildEvent> observer;

        Listener(@NonNull DatabaseReference ref,
                @NonNull Observer<? super ChildEvent> observer) {
            this.ref = ref;
            this.observer = observer;
        }

        @Override
        public void onChildAdded(@NonNull final DataSnapshot dataSnapshot,
                String previousChildName) {
            if (!isDisposed()) {
                observer.onNext(ChildAddEvent.create(dataSnapshot, previousChildName));
            }
        }

        @Override
        public void onChildChanged(@NonNull final DataSnapshot dataSnapshot,
                String previousChildName) {
            if (!isDisposed()) {
                observer.onNext(ChildChangeEvent.create(dataSnapshot, previousChildName));
            }
        }

        @Override
        public void onChildRemoved(@NonNull final DataSnapshot dataSnapshot) {
            if (!isDisposed()) {
                observer.onNext(ChildRemoveEvent.create(dataSnapshot));
            }
        }

        @Override
        public void onChildMoved(@NonNull final DataSnapshot dataSnapshot,
                String previousChildName) {
            if (!isDisposed()) {
                observer.onNext(ChildMoveEvent.create(dataSnapshot, previousChildName));
            }
        }

        @Override
        public void onCancelled(@NonNull final DatabaseError databaseError) {
            if (!isDisposed()) {
                observer.onError(databaseError.toException());
            }
        }

        @Override
        protected void onDispose() {
            ref.removeEventListener(this);
        }
    }
}
