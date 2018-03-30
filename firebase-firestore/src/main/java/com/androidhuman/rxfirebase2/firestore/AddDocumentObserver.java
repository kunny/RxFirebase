package com.androidhuman.rxfirebase2.firestore;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class AddDocumentObserver<T> extends Single<DocumentReference> {

    private final CollectionReference instance;

    private final T value;

    AddDocumentObserver(CollectionReference instance, T value) {
        this.instance = instance;
        this.value = value;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super DocumentReference> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.add(value)
                .addOnCompleteListener(listener);
    }

    static final class Listener extends OnCompleteDisposable<DocumentReference> {

        private final SingleObserver<? super DocumentReference> observer;

        Listener(@NonNull SingleObserver<? super DocumentReference> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<DocumentReference> task) {
            if (!isDisposed()) {
                if (task.isSuccessful()) {
                    observer.onSuccess(task.getResult());
                } else {
                    observer.onError(task.getException());
                }
            }
        }
    }
}
