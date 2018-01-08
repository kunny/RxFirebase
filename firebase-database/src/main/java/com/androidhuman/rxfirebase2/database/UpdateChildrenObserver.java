package com.androidhuman.rxfirebase2.database;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.androidhuman.rxfirebase2.core.SimpleDisposable;

import android.support.annotation.NonNull;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

final class UpdateChildrenObserver extends Completable {

    private final DatabaseReference instance;

    private final Map<String, Object> update;

    UpdateChildrenObserver(DatabaseReference instance, Map<String, Object> update) {
        this.instance = instance;
        this.update = update;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.updateChildren(update, listener);
    }

    static class Listener extends SimpleDisposable implements DatabaseReference.CompletionListener {

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
