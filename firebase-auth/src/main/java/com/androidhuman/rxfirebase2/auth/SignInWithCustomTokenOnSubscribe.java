package com.androidhuman.rxfirebase2.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class SignInWithCustomTokenOnSubscribe
        implements SingleOnSubscribe<FirebaseUser> {

    private final FirebaseAuth instance;

    private final String token;

    SignInWithCustomTokenOnSubscribe(FirebaseAuth instance, String token) {
        this.instance = instance;
        this.token = token;
    }

    @Override
    public void subscribe(final SingleEmitter<FirebaseUser> emitter) {
        final OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(task.getException());
                    }
                    return;
                }

                if (!emitter.isDisposed()) {
                    emitter.onSuccess(task.getResult().getUser());
                }
            }
        };

        instance.signInWithCustomToken(token)
                .addOnCompleteListener(listener);
    }
}
