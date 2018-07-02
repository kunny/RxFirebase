package com.androidhuman.rxfirebase2.firestore;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import com.androidhuman.rxfirebase2.core.SimpleDisposable;
import com.androidhuman.rxfirebase2.firestore.model.Value;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Observer;

final class DocumentChangesObserver extends Observable<Value<DocumentSnapshot>> {

    private final DocumentReference instance;

    DocumentChangesObserver(@NonNull DocumentReference instance) {
        this.instance = instance;
    }

    @Override
    protected void subscribeActual(Observer<? super Value<DocumentSnapshot>> observer) {
        Listener listener = new Listener(instance, observer);
        observer.onSubscribe(listener);
    }

    static final class Listener extends SimpleDisposable
            implements EventListener<DocumentSnapshot> {

        private final ListenerRegistration listenerRegistration;

        private final Observer<? super Value<DocumentSnapshot>> observer;

        Listener(@NonNull DocumentReference ref,
                @NonNull Observer<? super Value<DocumentSnapshot>> observer) {
            this.listenerRegistration = ref.addSnapshotListener(this);
            this.observer = observer;
        }

        @Override
        protected void onDispose() {
            listenerRegistration.remove();
        }

        @Override
        public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
            if (!isDisposed()) {
                if (null != e) {
                    observer.onError(e);
                } else {
                    if (null != snapshot && snapshot.exists()) {
                        observer.onNext(Value.of(snapshot));
                    } else {
                        observer.onNext(Value.<DocumentSnapshot>empty());
                    }
                }
            }
        }
    }
}
