package com.androidhuman.rxfirebase3.firestore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;

public final class SetMapValueObserver extends Completable {

    private final DocumentReference instance;

    private final Map<String, Object> value;

    @Nullable
    private final SetOptions options;

    SetMapValueObserver(@NonNull DocumentReference instance, Map<String, Object> value) {
        this(instance, value, null);
    }

    SetMapValueObserver(
            @NonNull DocumentReference instance,
            Map<String, Object> value, @Nullable SetOptions options) {
        this.instance = instance;
        this.value = value;
        this.options = options;
    }

    @Override
    protected void subscribeActual(@NonNull final CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        if (null != options) {
            instance.set(value, options)
                    .addOnCompleteListener(listener);
        } else {
            instance.set(value)
                    .addOnCompleteListener(listener);
        }
    }

    static final class Listener extends OnCompleteDisposable<Void> {

        private final CompletableObserver observer;

        Listener(@NonNull CompletableObserver observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<Void> task) {
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
