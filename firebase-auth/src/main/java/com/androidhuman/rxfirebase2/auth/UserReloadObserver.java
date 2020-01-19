package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

final class UserReloadObserver extends Completable {

    private final FirebaseUser user;

    UserReloadObserver(FirebaseUser user) {
        this.user = user;
    }

    @Override
    protected void subscribeActual(CompletableObserver observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        user.reload()
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
