package com.androidhuman.rxfirebase3.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.androidhuman.rxfirebase3.core.SimpleDisposable;

import android.support.annotation.NonNull;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

final class DataChangesObserver extends Observable<DataSnapshot> {

    private final DatabaseReference instance;

    DataChangesObserver(DatabaseReference instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(@NonNull final Observer<? super DataSnapshot> observer) {
        Listener listener = new Listener(instance, observer);
        observer.onSubscribe(listener);

        instance.addValueEventListener(listener);
    }

    private static final class Listener extends SimpleDisposable implements ValueEventListener {

        private final DatabaseReference ref;

        private final Observer<? super DataSnapshot> observer;

        Listener(@NonNull DatabaseReference ref,
                 @NonNull Observer<? super DataSnapshot> observer) {
            this.ref = ref;
            this.observer = observer;
        }

        @Override
        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
            if (!isDisposed()) {
                observer.onNext(dataSnapshot);
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
