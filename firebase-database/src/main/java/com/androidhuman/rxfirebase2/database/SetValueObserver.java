package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.androidhuman.rxfirebase2.database.core.SimpleDisposable;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

final class SetValueObserver<T> extends Completable {

    private final DatabaseReference instance;

    private final T value;

    SetValueObserver(DatabaseReference instance, T value) {
        this.instance = instance;
        this.value = value;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.setValue(value, listener);
    }

    static final class Listener extends SimpleDisposable
            implements DatabaseReference.CompletionListener {

        private final CompletableObserver observer;

        Listener(@NonNull CompletableObserver observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
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
