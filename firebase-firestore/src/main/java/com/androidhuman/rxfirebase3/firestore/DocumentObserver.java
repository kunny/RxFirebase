package com.androidhuman.rxfirebase3.firestore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;
import com.androidhuman.rxfirebase3.firestore.model.Value;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Source;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;

final class DocumentObserver extends Single<Value<DocumentSnapshot>> {

    private final DocumentReference instance;

    @Nullable
    private final Source source;

    DocumentObserver(@NonNull DocumentReference instance) {
        this(instance, null);
    }

    DocumentObserver(@NonNull DocumentReference instance, @Nullable Source source) {
        this.instance = instance;
        this.source = source;
    }

    @Override
    protected void subscribeActual(@NonNull final SingleObserver<? super Value<DocumentSnapshot>> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        if (null != source) {
            instance.get(source)
                    .addOnCompleteListener(listener);
        } else {
            instance.get()
                    .addOnCompleteListener(listener);
        }
    }

    static final class Listener extends OnCompleteDisposable<DocumentSnapshot> {

        private final SingleObserver<? super Value<DocumentSnapshot>> observer;

        Listener(@NonNull SingleObserver<? super Value<DocumentSnapshot>> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    Exception ex = task.getException();
                    if (null != ex) {
                        observer.onError(ex);
                    } else {
                        observer.onError(new UnknownError());
                    }
                } else {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        observer.onSuccess(Value.of(document));
                    } else {
                        observer.onSuccess(Value.<DocumentSnapshot>empty());
                    }
                }
            }
        }
    }
}
