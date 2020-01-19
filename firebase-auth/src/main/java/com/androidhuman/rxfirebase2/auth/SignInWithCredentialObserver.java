package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.SingleObserver;

final class SignInWithCredentialObserver extends Single<AuthResult> {

    private final FirebaseAuth instance;

    private final AuthCredential credential;

    SignInWithCredentialObserver(FirebaseAuth instance, AuthCredential credential) {
        this.instance = instance;
        this.credential = credential;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super AuthResult> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        instance.signInWithCredential(credential)
                .addOnCompleteListener(listener);
    }

    private final class Listener extends OnCompleteDisposable<AuthResult> {

        private final SingleObserver<? super AuthResult> observer;

        Listener(@NonNull SingleObserver<? super AuthResult> observer) {
            this.observer = observer;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (!isDisposed()) {
                if (!task.isSuccessful()) {
                    observer.onError(task.getException());
                } else {
                    observer.onSuccess(task.getResult());
                }
            }
        }
    }

}
