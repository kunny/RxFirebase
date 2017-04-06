package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import rx.Single;
import rx.SingleSubscriber;

class CreateUserWithEmailAndPasswordOnSubscribe
        implements Single.OnSubscribe<FirebaseUser> {

    private final FirebaseAuth instance;

    private final String email;

    private final String password;

    CreateUserWithEmailAndPasswordOnSubscribe(
            FirebaseAuth instance, String email, String password) {
        this.instance = instance;
        this.email = email;
        this.password = password;
    }

    @Override
    public void call(final SingleSubscriber<? super FirebaseUser> subscriber) {
        final OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(task.getException());
                    }
                    return;
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onSuccess(task.getResult().getUser());
                }
            }
        };

        instance.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }
}
