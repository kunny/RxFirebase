package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

final class UserSendEmailVerificationObserver extends Completable {

    private final FirebaseUser user;

    UserSendEmailVerificationObserver(FirebaseUser user) {
        this.user = user;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        user.sendEmailVerification()
                .addOnCompleteListener(listener);
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
                    observer.onError(task.getException());
                } else {
                    observer.onComplete();
                }
            }
        }
    }
}
