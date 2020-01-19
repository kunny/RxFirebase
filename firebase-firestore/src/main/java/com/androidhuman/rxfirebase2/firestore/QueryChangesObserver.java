package com.androidhuman.rxfirebase2.firestore;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.androidhuman.rxfirebase2.core.SimpleDisposable;
import com.androidhuman.rxfirebase2.firestore.model.Value;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Observer;

final class QueryChangesObserver extends Observable<Value<QuerySnapshot>> {

    private final Query query;

    QueryChangesObserver(@NonNull Query query) {
        this.query = query;
    }

    @Override
    protected void subscribeActual(Observer<? super Value<QuerySnapshot>> observer) {
        Listener listener = new Listener(query, observer);
        observer.onSubscribe(listener);
    }

    static final class Listener extends SimpleDisposable implements EventListener<QuerySnapshot> {

        private final ListenerRegistration listenerRegistration;

        private final Observer<? super Value<QuerySnapshot>> observer;

        Listener(@NonNull Query query,
                @NonNull Observer<? super Value<QuerySnapshot>> observer) {
            this.listenerRegistration = query.addSnapshotListener(this);
            this.observer = observer;
        }

        @Override
        protected void onDispose() {
            listenerRegistration.remove();
        }

        @Override
        public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {
            if (!isDisposed()) {
                if (e != null) {
                    observer.onError(e);
                } else {
                    if (!snapshot.isEmpty()) {
                        observer.onNext(Value.of(snapshot));
                    } else {
                        observer.onNext(Value.<QuerySnapshot>empty());
                    }
                }
            }
        }
    }
}
