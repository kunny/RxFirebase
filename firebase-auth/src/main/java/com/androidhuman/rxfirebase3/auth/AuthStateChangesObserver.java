package com.androidhuman.rxfirebase3.auth;

import com.google.firebase.auth.FirebaseAuth;

import com.androidhuman.rxfirebase3.core.SimpleDisposable;

import android.support.annotation.NonNull;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

final class AuthStateChangesObserver extends Observable<FirebaseAuth> {

    private final FirebaseAuth instance;

    AuthStateChangesObserver(FirebaseAuth instance) {
        this.instance = instance;
    }

    @Override
    public void subscribeActual(@NonNull Observer<? super FirebaseAuth> observer) {
        Listener listener = new Listener(instance, observer);
        observer.onSubscribe(listener);

        instance.addAuthStateListener(listener);
    }

    private static final class Listener extends SimpleDisposable
            implements FirebaseAuth.AuthStateListener {

        private final FirebaseAuth auth;

        private final Observer<? super FirebaseAuth> observer;

        Listener(@NonNull FirebaseAuth auth, @NonNull Observer<? super FirebaseAuth> observer) {
            this.auth = auth;
            this.observer = observer;
        }

        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (!isDisposed()) {
                observer.onNext(auth);
            }
        }

        @Override
        protected void onDispose() {
            auth.removeAuthStateListener(this);
        }
    }
}
