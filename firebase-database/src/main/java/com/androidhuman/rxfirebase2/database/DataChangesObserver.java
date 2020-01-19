package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.androidhuman.rxfirebase2.core.SimpleDisposable;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Observer;

final class DataChangesObserver extends Observable<DataSnapshot> {

    private final DatabaseReference instance;

    DataChangesObserver(DatabaseReference instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(Observer<? super DataSnapshot> observer) {
        Listener listener = new Listener(instance, observer);
        observer.onSubscribe(listener);

        instance.addValueEventListener(listener);
    }

    private final class Listener extends SimpleDisposable implements ValueEventListener {

        private final DatabaseReference ref;

        private final Observer<? super DataSnapshot> observer;

        Listener(@NonNull DatabaseReference ref,
                @NonNull Observer<? super DataSnapshot> observer) {
            this.ref = ref;
            this.observer = observer;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (!isDisposed()) {
                observer.onNext(dataSnapshot);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
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
