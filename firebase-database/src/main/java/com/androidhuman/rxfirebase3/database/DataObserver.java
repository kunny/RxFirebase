package com.androidhuman.rxfirebase3.database;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase3.core.SimpleDisposable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;

final class DataObserver extends Single<DataSnapshot> {

    private final DatabaseReference instance;

    DataObserver(DatabaseReference instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(@NonNull final SingleObserver<? super DataSnapshot> observer) {
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
        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
            if (!isDisposed()) {
                observer.onSuccess(dataSnapshot);
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
