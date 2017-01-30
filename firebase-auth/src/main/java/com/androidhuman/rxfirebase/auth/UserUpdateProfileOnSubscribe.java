package com.androidhuman.rxfirebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import android.support.annotation.NonNull;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class UserUpdateProfileOnSubscribe implements SingleOnSubscribe<TaskResult> {

    private final FirebaseUser user;

    private final UserProfileChangeRequest request;

    UserUpdateProfileOnSubscribe(FirebaseUser user, UserProfileChangeRequest request) {
        this.user = user;
        this.request = request;
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

        user.updateProfile(request)
                .addOnCompleteListener(listener);
    }
}
