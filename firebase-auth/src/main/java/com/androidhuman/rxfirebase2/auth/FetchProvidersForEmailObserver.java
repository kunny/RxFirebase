package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import com.androidhuman.rxfirebase2.auth.core.OnCompleteDisposable;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;

final class FetchProvidersForEmailObserver extends Maybe<List<String>> {

    private final FirebaseAuth instance;

    private final String email;

    FetchProvidersForEmailObserver(FirebaseAuth instance, String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    protected void subscribeActual(MaybeObserver<? super List<String>> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.fetchProvidersForEmail(email)
                .addOnCompleteListener(listener);
    }

    static final class Listener extends OnCompleteDisposable<ProviderQueryResult> {

        private final MaybeObserver<? super List<String>> observer;

        Listener(@NonNull MaybeObserver<? super List<String>> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    List<String> providers = task.getResult().getProviders();
                    if (null != providers) {
                        observer.onSuccess(providers);
                    } else {
                        observer.onComplete();
                    }
                }
            }
        }
    }
}
