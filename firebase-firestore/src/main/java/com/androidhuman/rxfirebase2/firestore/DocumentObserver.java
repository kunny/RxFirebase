package com.androidhuman.rxfirebase2.firestore;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase2.firestore.stuff.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class DocumentObserver extends Single<DocumentSnapshot> {

    private final DocumentReference instance;

    DocumentObserver(DocumentReference instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super DocumentSnapshot> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.get()
                .addOnCompleteListener(listener);
    }

    static final class Listener extends OnCompleteDisposable<DocumentSnapshot> {

        private final SingleObserver<? super DocumentSnapshot> observer;

        Listener(@NonNull SingleObserver<? super DocumentSnapshot> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
