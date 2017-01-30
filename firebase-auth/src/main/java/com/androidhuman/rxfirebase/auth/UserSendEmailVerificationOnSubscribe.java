package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import android.support.annotation.NonNull;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class UserSendEmailVerificationOnSubscribe implements SingleOnSubscribe<TaskResult> {

    private final FirebaseUser user;

    UserSendEmailVerificationOnSubscribe(FirebaseUser user) {
        this.user = user;
    }

    @Override
    public void subscribe(final SingleEmitter<TaskResult> emitter) {
        OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!emitter.isDisposed()) {
                    if (!task.isSuccessful()) {
                        emitter.onSuccess(TaskResult.failure(task.getException()));
                    } else {
                        emitter.onSuccess(TaskResult.success());
                    }
                }
            }
        };

        user.sendEmailVerification()
                .addOnCompleteListener(listener);
    }
}
