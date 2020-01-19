package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.androidhuman.rxfirebase2.core.SimpleDisposable;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

final class RemoveValueObserver extends Completable {

    private final DatabaseReference instance;

    RemoveValueObserver(DatabaseReference instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
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
        public void onComplete(DatabaseError databaseError,
                @NonNull DatabaseReference databaseReference) {
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
