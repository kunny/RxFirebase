package com.androidhuman.rxfirebase2.firestore;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase2.firestore.stuff.SimpleDisposable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import io.reactivex.Observable;
import io.reactivex.Observer;

final class QueryChangesObserver extends Observable<QuerySnapshot> {

    private final Query query;

    QueryChangesObserver(Query query) {
        this.query = query;
    }

    @Override
    protected void subscribeActual(Observer<? super QuerySnapshot> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        listener.listenerRegistration = query.addSnapshotListener(listener);
    }

    static final class Listener extends SimpleDisposable implements EventListener<QuerySnapshot> {

        private final Observer<? super QuerySnapshot> observer;
        ListenerRegistration listenerRegistration;

        Listener(@NonNull Observer<? super QuerySnapshot> observer) {
            this.observer = observer;
        }

        @Override
        protected void onDispose() {
            listenerRegistration.remove();
        }

        @Override
        public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
            if (!isDisposed()) {
                if (e != null) {
                    observer.onError(e);
                    return;
                }
                observer.onNext(querySnapshot);
            }
        }
    }
}
