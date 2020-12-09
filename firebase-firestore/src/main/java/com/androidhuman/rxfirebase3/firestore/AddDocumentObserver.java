package com.androidhuman.rxfirebase3.firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;

import android.support.annotation.NonNull;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;

final class AddDocumentObserver<T> extends Single<DocumentReference> {

    private final CollectionReference instance;

    private final T value;

    AddDocumentObserver(@NonNull CollectionReference instance, T value) {
        this.instance = instance;
        this.value = value;
    }

    @Override
    protected void subscribeActual(
            @NonNull final SingleObserver<? super DocumentReference> observer) {
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
                if (!task.isSuccessful()) {
                    Exception ex = task.getException();
                    if (null != ex) {
                        observer.onError(ex);
                    } else {
                        observer.onError(new UnknownError());
                    }
                } else {
                    observer.onSuccess(task.getResult());
                }
            }
        }
    }
}
