package com.androidhuman.rxfirebase3.database;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase3.core.SimpleDisposable;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;

final class UpdateChildrenObserver extends Completable {

    private final DatabaseReference instance;

    private final Map<String, Object> update;

    UpdateChildrenObserver(DatabaseReference instance, Map<String, Object> update) {
        this.instance = instance;
        this.update = update;
    }

    @Override
    protected void subscribeActual(@NonNull final CompletableObserver observer) {
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
        public void onComplete(DatabaseError databaseError, @NonNull final DatabaseReference databaseReference) {
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
