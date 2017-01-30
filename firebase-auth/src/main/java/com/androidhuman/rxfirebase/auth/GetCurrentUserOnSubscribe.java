package com.androidhuman.rxfirebase.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.memoizrlabs.retrooptional.Optional;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class GetCurrentUserOnSubscribe implements SingleOnSubscribe<Optional<FirebaseUser>> {

    private FirebaseAuth instance;

    GetCurrentUserOnSubscribe(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    public void subscribe(SingleEmitter<Optional<FirebaseUser>> emitter) {
        if (!emitter.isDisposed()) {
            emitter.onSuccess(Optional.of(instance.getCurrentUser()));
        }
    }
}
