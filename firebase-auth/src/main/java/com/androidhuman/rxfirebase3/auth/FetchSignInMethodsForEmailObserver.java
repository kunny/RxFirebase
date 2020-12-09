package com.androidhuman.rxfirebase3.auth;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;

final class FetchSignInMethodsForEmailObserver extends Maybe<List<String>> {

    private final FirebaseAuth instance;

    private final String email;

    FetchSignInMethodsForEmailObserver(
            @NonNull FirebaseAuth instance, @NonNull String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    protected void subscribeActual(@NonNull MaybeObserver<? super List<String>> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(listener);
    }

    private static final class Listener extends OnCompleteDisposable<SignInMethodQueryResult> {

        private final MaybeObserver<? super List<String>> observer;

        Listener(@NonNull MaybeObserver<? super List<String>> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    List<String> methods = task.getResult().getSignInMethods();
                    if (null != methods) {
                        observer.onSuccess(methods);
                    } else {
                        observer.onComplete();
                    }
                }
            }
        }
    }
}
