package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.support.annotation.NonNull;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

final class SendPasswordResetEmailOnSubscribe implements CompletableOnSubscribe {

    private final FirebaseAuth instance;

    private final String email;

    SendPasswordResetEmailOnSubscribe(FirebaseAuth instance, String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    public void subscribe(final CompletableEmitter emitter) {
        final OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    emitter.onError(task.getException());
                    return;
                }

                emitter.onComplete();
            }
        };

        instance.sendPasswordResetEmail(email)
                .addOnCompleteListener(listener);
    }
}
