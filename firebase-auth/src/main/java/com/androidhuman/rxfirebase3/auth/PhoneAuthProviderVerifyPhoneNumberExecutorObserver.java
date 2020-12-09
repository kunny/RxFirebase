package com.androidhuman.rxfirebase3.auth;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

public final class PhoneAuthProviderVerifyPhoneNumberExecutorObserver
        extends Observable<PhoneAuthEvent> {

    private final PhoneAuthProvider provider;

    private final String phoneNumber;

    private final long timeOut;

    private final TimeUnit timeUnit;

    private final Executor executor;

    @Nullable
    private final PhoneAuthProvider.ForceResendingToken forceResendingToken;

    PhoneAuthProviderVerifyPhoneNumberExecutorObserver(
            PhoneAuthProvider provider, String phoneNumber, long timeOut,
            TimeUnit timeUnit, Executor executor,
            @Nullable PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        this.provider = provider;
        this.phoneNumber = phoneNumber;
        this.timeOut = timeOut;
        this.timeUnit = timeUnit;
        this.executor = executor;
        this.forceResendingToken = forceResendingToken;
    }

    @Override
    protected void subscribeActual(@NonNull Observer<? super PhoneAuthEvent> observer) {
        Listener listener = new Listener(observer);
        observer.onSubscribe(listener);

        if (null == forceResendingToken) {
            provider.verifyPhoneNumber(phoneNumber, timeOut, timeUnit,
                    executor, listener);
        } else {
            provider.verifyPhoneNumber(phoneNumber, timeOut, timeUnit,
                    executor, listener, forceResendingToken);
        }
    }

    private static final class Listener extends OnVerificationStateChangedDisposable {

        private final Observer<? super PhoneAuthEvent> observer;

        Listener(@NonNull Observer<? super PhoneAuthEvent> observer) {
            this.observer = observer;
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String verificationId) {
            if (!isDisposed()) {
                observer.onNext(PhoneAuthCodeAutoRetrievalTimeOutEvent.create(verificationId));
                observer.onComplete();
            }
        }

        @Override
        public void onCodeSent(String verificationId,
                PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            if (!isDisposed()) {
                observer.onNext(PhoneAuthCodeSentEvent.create(verificationId, forceResendingToken));
            }
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            if (!isDisposed()) {
                observer.onNext(PhoneAuthVerificationCompleteEvent.create(credential));
                observer.onComplete();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (!isDisposed()) {
                observer.onError(e);
            }
        }
    }
}
