package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import rx.Single;
import rx.SingleSubscriber;

final class SignInWithCredentialOnSubscribe
        implements Single.OnSubscribe<FirebaseUser> {

    private final FirebaseAuth instance;

    private final AuthCredential credential;

    SignInWithCredentialOnSubscribe(FirebaseAuth instance, AuthCredential credential) {
        this.instance = instance;
        this.credential = credential;
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
                    FirebaseUser user = task.getResult().getUser();
                    if (null != user) {
                        subscriber.onSuccess(user);
                    } else {
                        subscriber.onError(
                                new IllegalStateException(("FirebaseUser does not exists.")));
                    }
                }
            }
        };

        instance.signInWithCredential(credential)
                .addOnCompleteListener(listener);
    }
}
