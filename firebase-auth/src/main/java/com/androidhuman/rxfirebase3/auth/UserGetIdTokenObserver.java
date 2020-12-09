package com.androidhuman.rxfirebase3.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;

import android.support.annotation.NonNull;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;

final class UserGetIdTokenObserver extends Single<String> {

    private final FirebaseUser user;

    private final boolean forceRefresh;

    UserGetIdTokenObserver(FirebaseUser user, boolean forceRefresh) {
        this.user = user;
        this.forceRefresh = forceRefresh;
    }

    @Override
    protected void subscribeActual(@NonNull final SingleObserver<? super String> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        user.getIdToken(forceRefresh)
                .addOnCompleteListener(listener);
    }

    private static final class Listener extends OnCompleteDisposable<GetTokenResult> {

        private final SingleObserver<? super String> observer;

        Listener(@NonNull SingleObserver<? super String> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<GetTokenResult> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    observer.onSuccess(task.getResult().getToken());
                }
            }
        }
    }
}
