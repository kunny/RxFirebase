package com.androidhuman.rxfirebase3.auth;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;

final class UserUnlinkObserver extends Single<FirebaseUser> {

    private final FirebaseUser user;

    private final String provider;

    UserUnlinkObserver(FirebaseUser user, String provider) {
        this.user = user;
        this.provider = provider;
    }

    @Override
    protected void subscribeActual(@NonNull final SingleObserver<? super FirebaseUser> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        user.unlink(provider)
                .addOnCompleteListener(listener);
    }

    private static final class Listener extends OnCompleteDisposable<AuthResult> {

        private final SingleObserver<? super FirebaseUser> observer;

        Listener(@NonNull SingleObserver<? super FirebaseUser> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    observer.onSuccess(task.getResult().getUser());
                }
            }
        }
    }
}
