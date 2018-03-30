package com.androidhuman.rxfirebase2.firestore;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase2.firestore.stuff.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class QueryObserver extends Single<QuerySnapshot> {

    private final Query query;

    QueryObserver(Query query) {
        this.query = query;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super QuerySnapshot> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        query.get()
                .addOnCompleteListener(listener);
    }

    static final class Listener extends OnCompleteDisposable<QuerySnapshot> {

        private final SingleObserver<? super QuerySnapshot> observer;

        Listener(@NonNull SingleObserver<? super QuerySnapshot> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
