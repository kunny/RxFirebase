package com.androidhuman.rxfirebase3.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.rxfirebase3.core.OnCompleteDisposable;

import android.support.annotation.NonNull;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;

final class UpdateCurrentUserObserver extends Completable {

    private final FirebaseAuth auth;

    private final FirebaseUser user;

    UpdateCurrentUserObserver(
            @NonNull FirebaseAuth auth, @NonNull FirebaseUser user) {
        this.auth = auth;
        this.user = user;
    }

    @Override
    protected void subscribeActual(@NonNull final CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        auth.updateCurrentUser(user)
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
