package com.androidhuman.rxfirebase2.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import com.androidhuman.rxfirebase2.core.SimpleDisposable;

import android.support.annotation.NonNull;

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

        instance.removeValue()
                .addOnCompleteListener(listener);
    }

    static final class Listener extends SimpleDisposable implements OnCompleteListener<Void> {

        private final CompletableObserver observer;

        Listener(@NonNull CompletableObserver observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
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
