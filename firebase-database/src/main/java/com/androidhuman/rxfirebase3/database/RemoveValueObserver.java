package com.androidhuman.rxfirebase3.database;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.androidhuman.rxfirebase3.core.SimpleDisposable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;

final class RemoveValueObserver extends Completable {

    private final DatabaseReference instance;

    RemoveValueObserver(DatabaseReference instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(@NonNull final CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.removeValue(listener);
    }

    static final class Listener extends SimpleDisposable
            implements DatabaseReference.CompletionListener {

        private final CompletableObserver observer;

        Listener(@NonNull CompletableObserver observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@Nullable final DatabaseError databaseError,
                               @NonNull final DatabaseReference databaseReference) {
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
