package com.androidhuman.rxfirebase.auth;

import com.google.firebase.auth.FirebaseAuth;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class SignOutOnSubscribe implements SingleOnSubscribe<TaskResult> {

    private final FirebaseAuth instance;

    SignOutOnSubscribe(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    public void subscribe(SingleEmitter<TaskResult> emitter) {
        if (!emitter.isDisposed()) {
            instance.signOut();
            emitter.onSuccess(TaskResult.success());
        }
    }
}
