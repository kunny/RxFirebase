package com.androidhuman.rxfirebase3.firestore;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase3.core.SimpleDisposable;
import com.androidhuman.rxfirebase3.firestore.model.Value;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

final class QueryChangesObserver extends Observable<Value<QuerySnapshot>> {

    private final Query query;

    QueryChangesObserver(@NonNull Query query) {
        this.query = query;
    }

    @Override
    protected void subscribeActual(@NonNull final Observer<? super Value<QuerySnapshot>> observer) {
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
