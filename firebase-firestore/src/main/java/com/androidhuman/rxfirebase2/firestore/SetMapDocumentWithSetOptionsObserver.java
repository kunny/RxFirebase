package com.androidhuman.rxfirebase2.firestore;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase2.firestore.stuff.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

final class SetMapDocumentWithSetOptionsObserver extends Completable {

    private final DocumentReference instance;

    private final Map<String, Object> value;

    private final SetOptions setOptions;

    SetMapDocumentWithSetOptionsObserver(DocumentReference instance, Map<String, Object> value, SetOptions setOptions) {
        this.instance = instance;
        this.value = value;
        this.setOptions = setOptions;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.set(value, setOptions)
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
                    observer.onError(task.getException());
                } else {
                    observer.onComplete();
                }
            }
        }
    }
}
