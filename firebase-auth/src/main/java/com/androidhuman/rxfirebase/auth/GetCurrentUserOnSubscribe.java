package com.androidhuman.rxfirebase.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;

final class GetCurrentUserOnSubscribe implements MaybeOnSubscribe<FirebaseUser> {

    private FirebaseAuth instance;

    GetCurrentUserOnSubscribe(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    public void subscribe(MaybeEmitter<FirebaseUser> emitter) throws Exception {
        if (!emitter.isDisposed()) {
            FirebaseUser user = instance.getCurrentUser();
            if (null != user) {
                emitter.onSuccess(user);
            }
            emitter.onComplete();
        }
    }
}
