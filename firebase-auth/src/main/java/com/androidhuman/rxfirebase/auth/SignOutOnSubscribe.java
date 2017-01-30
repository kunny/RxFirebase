package com.androidhuman.rxfirebase.auth;

import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

final class SignOutOnSubscribe implements CompletableOnSubscribe {

    private final FirebaseAuth instance;

    SignOutOnSubscribe(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    public void subscribe(CompletableEmitter emitter) {
        if (!emitter.isDisposed()) {
            instance.signOut();
            emitter.onComplete();
        }
    }
}
