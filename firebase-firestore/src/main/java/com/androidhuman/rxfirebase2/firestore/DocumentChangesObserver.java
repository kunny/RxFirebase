package com.androidhuman.rxfirebase2.firestore;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase2.firestore.stuff.SimpleDisposable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import io.reactivex.Observable;
import io.reactivex.Observer;

final class DocumentChangesObserver extends Observable<DocumentSnapshot> {

    private final DocumentReference instance;

    DocumentChangesObserver(DocumentReference instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(Observer<? super DocumentSnapshot> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        listener.listenerRegistration = instance.addSnapshotListener(listener);
    }

    static final class Listener extends SimpleDisposable implements EventListener<DocumentSnapshot> {

        private final Observer<? super DocumentSnapshot> observer;
        ListenerRegistration listenerRegistration;

        Listener(@NonNull Observer<? super DocumentSnapshot> observer) {
            this.observer = observer;
        }

        @Override
        protected void onDispose() {
            listenerRegistration.remove();
        }

        @Override
        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
            if (!isDisposed()) {
                if (e != null) {
                    observer.onError(e);
                    return;
                }

                observer.onNext(documentSnapshot);
            }
        }
    }
}
