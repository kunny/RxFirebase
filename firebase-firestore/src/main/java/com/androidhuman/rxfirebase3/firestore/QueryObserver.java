package com.androidhuman.rxfirebase3.firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;
import com.androidhuman.rxfirebase3.firestore.model.Value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;

final class QueryObserver extends Single<Value<QuerySnapshot>> {

    private final Query query;

    @Nullable
    private final Source source;

    QueryObserver(@NonNull Query query) {
        this(query, null);
    }

    QueryObserver(@NonNull Query query, @Nullable Source source) {
        this.query = query;
        this.source = source;
    }

    @Override
    protected void subscribeActual(
            @NonNull final SingleObserver<? super Value<QuerySnapshot>> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        if (null != source) {
            query.get(source)
                    .addOnCompleteListener(listener);
        } else {
            query.get()
                    .addOnCompleteListener(listener);
        }
    }

    static final class Listener extends OnCompleteDisposable<QuerySnapshot> {

        private final SingleObserver<? super Value<QuerySnapshot>> observer;

        Listener(@NonNull SingleObserver<? super Value<QuerySnapshot>> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    Exception ex = task.getException();
                    if (null != ex) {
                        observer.onError(ex);
                    } else {
                        observer.onError(new UnknownError());
                    }
                } else {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null && !snapshot.isEmpty()) {
                        observer.onSuccess(Value.of(snapshot));
                    } else {
                        observer.onSuccess(Value.<QuerySnapshot>empty());
                    }
                }
            }
        }
    }
}
