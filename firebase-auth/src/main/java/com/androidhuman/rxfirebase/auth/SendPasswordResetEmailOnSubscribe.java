package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import android.support.annotation.NonNull;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class SendPasswordResetEmailOnSubscribe implements SingleOnSubscribe<TaskResult> {

    private final FirebaseAuth instance;

    private final String email;

    SendPasswordResetEmailOnSubscribe(FirebaseAuth instance, String email) {
        this.instance = instance;
        this.email = email;
    }

    @Override
    public void subscribe(final SingleEmitter<TaskResult> emitter) {
        final OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    emitter.onSuccess(TaskResult.failure(task.getException()));
                    return;
                }

                emitter.onSuccess(TaskResult.success());
            }
        };

        instance.sendPasswordResetEmail(email)
                .addOnCompleteListener(listener);
    }
}
