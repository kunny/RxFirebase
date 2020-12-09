package com.androidhuman.rxfirebase3.auth;

import android.support.annotation.NonNull;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;

final class UserSendEmailVerificationObserver extends Completable {

    private final FirebaseUser user;

    UserSendEmailVerificationObserver(FirebaseUser user) {
        this.user = user;
    }

    @Override
    protected void subscribeActual(@NonNull final CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        user.sendEmailVerification()
                .addOnCompleteListener(listener);
    }

    private static final class Listener extends OnCompleteDisposable<Void> {

        private final CompletableObserver observer;

        Listener(@NonNull CompletableObserver observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    observer.onComplete();
                }
            }
        }
    }
}
