package com.androidhuman.rxfirebase3.firestore;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;

final class DeleteDocumentObserver extends Completable {

    private final DocumentReference instance;

    DeleteDocumentObserver(@NonNull DocumentReference instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(@NonNull final CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.delete()
                .addOnCompleteListener(listener);
    }

    static final class Listener extends OnCompleteDisposable<Void> {

        private final CompletableObserver observer;

        Listener(@NonNull CompletableObserver observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    Exception ex = task.getException();
                    if (null != ex) {
                        observer.onError(ex);
                    } else {
                        observer.onError(new UnknownError());
                    }
                } else {
                    observer.onComplete();
                }
            }
        }
    }
}
