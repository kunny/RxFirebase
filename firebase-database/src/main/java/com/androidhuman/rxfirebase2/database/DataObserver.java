package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.androidhuman.rxfirebase2.core.SimpleDisposable;

import android.support.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class DataObserver extends Single<DataSnapshot> {

    private final DatabaseReference instance;

    DataObserver(DatabaseReference instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super DataSnapshot> observer) {
        Listener listener = new Listener(instance, observer);
        observer.onSubscribe(listener);

        instance.addListenerForSingleValueEvent(listener);
    }

    static final class Listener extends SimpleDisposable implements ValueEventListener {

        private final DatabaseReference ref;

        private final SingleObserver<? super DataSnapshot> observer;

        Listener(@NonNull DatabaseReference ref,
                @NonNull SingleObserver<? super DataSnapshot> observer) {
            this.ref = ref;
            this.observer = observer;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!isDisposed()) {
                observer.onSuccess(dataSnapshot);
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
            ref.removeEventListener(this);
        }
    }
}
