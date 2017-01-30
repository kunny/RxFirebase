package com.androidhuman.rxfirebase.auth;

import com.google.firebase.auth.FirebaseAuth;

import android.support.annotation.NonNull;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;


final class AuthStateChangesOnSubscribe implements ObservableOnSubscribe<FirebaseAuth> {

    private final FirebaseAuth instance;

    AuthStateChangesOnSubscribe(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    public void subscribe(final ObservableEmitter<FirebaseAuth> emitter) {
        final FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(firebaseAuth);
                }
            }
        };

        instance.addAuthStateListener(listener);

        emitter.setDisposable(Disposables.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                instance.removeAuthStateListener(listener);
            }
        }));
    }
}
